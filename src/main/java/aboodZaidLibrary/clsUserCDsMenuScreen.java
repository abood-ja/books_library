package aboodZaidLibrary;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsUserCDsMenuScreen extends clsScreen {

    private static final String SEP = "#//#";
    private static final String LOANS_FILE = "Loans.txt";

    private static void __ensureLoansFile() {
        try {
            Path p = Paths.get(LOANS_FILE);
            if (Files.notExists(p)) Files.createFile(p);
        } catch (IOException e) {
            System.out.println("⚠️ Cannot create Loans file.");
        }
    }

    private static void _BorrowCD() {
        __ensureLoansFile();

        String pad = String.format("%37s", "");
        System.out.flush();
        _DrawScreenHeader("\t\tBorrow CD");

        if (clsUserSession.currentUser == null || clsUserSession.currentUser.isEmpty()) {
            System.out.println(pad + "❌ You must be logged in.");
            return;
        }

        String username = clsUserSession.currentUser.getUserName().trim().toLowerCase();

        if (__userHasOverdueCDs(username)) {
            System.out.println(pad + "❌ You cannot borrow a CD: you have overdue CDs.");
            return;
        }

        System.out.print(pad + "Enter CD ID: ");
        String cdid = clsInputValidate.readString().trim();

        if (!clsCD.isCDExist(cdid)) {
            System.out.println(pad + "❌ CD not found.");
            return;
        }

        if (__isCDCurrentlyBorrowed(cdid)) {
            System.out.println(pad + "❌ CD is already borrowed.");
            return;
        }

        LocalDate borrow = LocalDate.now();
        LocalDate due = borrow.plusDays(7);

        String line = "CD" + SEP + cdid + SEP + username + SEP + borrow + SEP + due + SEP + "false";

        try {
            Files.write(Paths.get(LOANS_FILE), Arrays.asList(line), StandardOpenOption.APPEND);
            System.out.println(pad + "✅ CD Borrowed Successfully! Due: " + due);
        } catch (IOException e) {
            System.out.println(pad + "❌ Failed to save loan.");
        }
    }

    private static boolean __isCDCurrentlyBorrowed(String cdid) {
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) return false;

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(Pattern.quote(SEP));

                if (p.length >= 6) {
                    String type = p[0].trim();
                    String id = p[1].trim();
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (type.equals("CD") && id.equals(cdid) && !returned)
                        return true;
                }
            }
        } catch (IOException e) {}

        return false;
    }

    private static boolean __userHasOverdueCDs(String username) {
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) return false;

        LocalDate today = LocalDate.now();

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(Pattern.quote(SEP));

                if (p.length >= 6) {
                    String type = p[0].trim();
                    String user = p[2].trim().toLowerCase();
                    LocalDate due = LocalDate.parse(p[4].trim());
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (type.equals("CD") && user.equals(username) && !returned && today.isAfter(due))
                        return true;
                }
            }
        } catch (IOException e) {}

        return false;
    }


    private static void _ReturnCD() {
        __ensureLoansFile();
        String pad = String.format("%37s", "");

        System.out.flush();
        _DrawScreenHeader("\t\tReturn CD");

        if (clsUserSession.currentUser == null || clsUserSession.currentUser.isEmpty()) {
            System.out.println(pad + "❌ Login required.");
            return;
        }

        String username = clsUserSession.currentUser.getUserName().trim().toLowerCase();

        Path f = Paths.get(LOANS_FILE);


        try {
            List<String> lines = Files.readAllLines(f);
            List<Integer> userLoans = new ArrayList<>();
            int index = 1;

            System.out.println(pad + "Your borrowed CDs:");

            for (int i = 0; i < lines.size(); i++) {
                String ln = lines.get(i).trim();
                if (ln.isEmpty()) continue;

                String[] p = ln.split(Pattern.quote(SEP));

                if (p.length >= 6) {
                    String type = p[0].trim();
                    String id = p[1].trim();
                    String user = p[2].trim().toLowerCase();
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (type.equals("CD") && user.equals(username) && !returned) {
                        System.out.println(pad + index + ". CD ID: " + id + " | Due: " + p[4]);
                        userLoans.add(i);
                        index++;
                    }
                }
            }

            if (userLoans.isEmpty()) {
                System.out.println(pad + "No active borrowed CDs.");
                return;
            }

            System.out.print(pad + "Choose which CD to return: ");
            int choice = clsInputValidate.readIntNumberBetween(1, userLoans.size());

            int lineIndex = userLoans.get(choice - 1);
            String[] p = lines.get(lineIndex).split(Pattern.quote(SEP));
            p[p.length - 1] = "true";

            lines.set(lineIndex, String.join(SEP, p));
            Files.write(f, lines);

            System.out.println(pad + "✅ CD returned successfully!");

        } catch (IOException e) {
            System.out.println(pad + "❌ Error updating file.");
        }
    }


    private static void _MyOverdueCDs() {
        __ensureLoansFile();
        String pad = String.format("%37s", "");

        System.out.flush();
        _DrawScreenHeader("\t\tMy Overdue CDs");

        if (clsUserSession.currentUser == null || clsUserSession.currentUser.isEmpty()) {
            System.out.println(pad + "❌ Login required.");
            return;
        }

        String username = clsUserSession.currentUser.getUserName().trim().toLowerCase();
        Path f = Paths.get(LOANS_FILE);

        LocalDate today = LocalDate.now();
        boolean found = false;

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(Pattern.quote(SEP));

                if (p.length >= 6) {
                    String type = p[0].trim();
                    String id = p[1].trim();
                    String user = p[2].trim().toLowerCase();
                    LocalDate due = LocalDate.parse(p[4].trim());
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (type.equals("CD") && user.equals(username)
                            && !returned && today.isAfter(due)) {

                        long days = ChronoUnit.DAYS.between(due, today);
                        System.out.println(pad + "- CD ID: " + id + " | Overdue by: " + days + " days");
                        found = true;
                    }
                }
            }
        } catch (IOException ignored) {}

        if (!found)
            System.out.println(pad + "No overdue CDs 👍");
    }



    public enum enCDMenuOptions {
        eList(1), eBorrow(2), eReturn(3), eOverdue(4), eFind(5), eBack(6);

        private final int value;
        enCDMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enCDMenuOptions _ReadCDMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose [1-6]: ");
        int choice = clsInputValidate.readIntNumberBetween(1, 6);

        for (enCDMenuOptions o : enCDMenuOptions.values())
            if (o.getValue() == choice) return o;

        return enCDMenuOptions.eBack;
    }

    private static void _GoBackToMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back...");
        new java.util.Scanner(System.in).nextLine();
        showCDsMenu();
    }

    private static void _PerformOption(enCDMenuOptions option) {
        switch (option) {
            case eList:
                System.out.print("\033[H\033[2J");
                clsCDsListScreen.showCDsListScreen();
                _GoBackToMenu();
                break;

            case eBorrow:
                System.out.print("\033[H\033[2J");
                _BorrowCD();
                _GoBackToMenu();
                break;

            case eReturn:
                System.out.print("\033[H\033[2J");
                _ReturnCD();
                _GoBackToMenu();
                break;

            case eOverdue:
                System.out.print("\033[H\033[2J");
                _MyOverdueCDs();
                _GoBackToMenu();
                break;

            case eFind:
                System.out.print("\033[H\033[2J");
                clsFindCDScreen.showFindCDScreen();
                _GoBackToMenu();
                break;

            case eBack:
                break;
        }
    }

    public static void showCDsMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tCDs Menu");

        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] CDs List");
        System.out.println(pad + "\t[2] Borrow CD");
        System.out.println(pad + "\t[3] Return CD");
        System.out.println(pad + "\t[4] My Overdue CDs");
        System.out.println(pad + "\t[5] Find CD");
        System.out.println(pad + "\t[6] Back");
        System.out.println(pad + "===========================================");

        _PerformOption(_ReadCDMenuOption());
    }
}
