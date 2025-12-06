package aboodZaidLibrary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsUserBooksMenuScreen extends clsScreen {

    private static final String SEP = "#//#";
    private static final String BOOKS_FILE = "Books.txt";
    private static final String LOANS_FILE = "Loans.txt";

    private static void __ensureLoansFile() {
        try {
            Path p = Paths.get(LOANS_FILE);
            if (Files.notExists(p)) Files.createFile(p);
        } catch (IOException e) {
            System.out.println("⚠️ Cannot create Loans file: " + e.getMessage());
        }
    }

    private static boolean __bookExistsByIsbn(String isbn) {
        Path p = Paths.get(BOOKS_FILE);
        if (!Files.exists(p)) return false;

        try {
            for (String line : Files.readAllLines(p)) {
                if (line == null || line.trim().isEmpty()) continue;
                String[] cols = line.split(Pattern.quote(SEP), -1);
                for (String cell : cols)
                    if (cell.trim().equalsIgnoreCase(isbn)) return true;
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading " + BOOKS_FILE + ": " + e.getMessage());
        }
        return false;
    }

    private static boolean __isCurrentlyBorrowed(String isbn) {
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) return false;

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP), -1);
                if (p.length >= 5) {
                    boolean returned = Boolean.parseBoolean(p[4].trim());
                    if (p[0].trim().equalsIgnoreCase(isbn) && !returned) return true;
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading loans: " + e.getMessage());
        }
        return false;
    }

    private static boolean __saveLoan(String isbn, String username, LocalDate borrow, LocalDate due) {
        String line = String.join(SEP, isbn, username, borrow.toString(), due.toString(), "false");

        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(LOANS_FILE), StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("⚠️ Error saving loan: " + e.getMessage());
            return false;
        }
    }

    private static void _ShowBorrowBookScreen() {
        String pad = String.format("%37s", "");
        _DrawScreenHeader("\t\tBorrow Book");
        __ensureLoansFile();

        if (!clsUserSession.currentUser.canBorrow()) {
            System.out.println(pad + "❌ Borrowing blocked. Resolve issues first.");
            return;
        }

        String username = clsUserSession.currentUser.getUserName().trim();
        System.out.println(pad + "Active user: " + username);

        Scanner sc = new Scanner(System.in);
        System.out.print(pad + "Enter ISBN to borrow: ");
        String isbn = sc.nextLine().trim();

        if (isbn.isEmpty()) {
            System.out.println(pad + "❌ ISBN cannot be empty.");
            return;
        }

        if (!__bookExistsByIsbn(isbn)) {
            System.out.println(pad + "❌ Book not found.");
            return;
        }

        if (__isCurrentlyBorrowed(isbn)) {
            System.out.println(pad + "❌ Already borrowed.");
            return;
        }

        LocalDate borrow = LocalDate.now();
        LocalDate due = borrow.plusDays(28);

        if (__saveLoan(isbn, username, borrow, due)) {
            System.out.println(pad + "✅ Borrowed successfully! Due: " + due);
        } else {
            System.out.println(pad + "⚠️ Could not save.");
        }
    }

    private static void _ShowBooksListScreen() {
        clsBooksListScreen.showBooksListScreen();
    }

    private static void _ShowFindBookScreen() {
        clsFindBookScreen.showFindBookScreen();
    }

    private static void _ShowMyOverdueBooksScreen() {
        String pad = String.format("%37s", "");
        _DrawScreenHeader("\t\tMy Overdue Books");

        String username = clsUserSession.currentUser.getUserName().toLowerCase();
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) {
            System.out.println(pad + "No loans found.");
            return;
        }

        LocalDate today = LocalDate.now();
        boolean any = false;

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP));

                if (p.length >= 5) {
                    String isbn = p[0].trim();
                    String u = p[1].trim().toLowerCase();
                    LocalDate due = LocalDate.parse(p[3].trim());
                    boolean returned = Boolean.parseBoolean(p[4].trim());

                    if (!returned && u.equals(username) && today.isAfter(due)) {
                        long days = ChronoUnit.DAYS.between(due, today);
                        System.out.println(pad + "- ISBN: " + isbn + " | Overdue: " + days + " days");
                        any = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }

        if (!any)
            System.out.println(pad + "You have no overdue items.");
    }

    private static void _ShowReturnBookScreen() {
        _ShowReturnBookScreen();
    }

    // ===== Added Pay Fine Screen =====
    private static void _ShowPayFineScreen() {
        clsPayFineScreen.showPayFineScreen();
    }

    public enum enBooksMenuOptions {
        eBooksList(1),
        eBorrowBook(2),
        eFindBook(3),
        eBackToMainMenu(4),
        eMyOverdueBooks(5),
        eReturnBook(6);

        private final int value;
        enBooksMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enBooksMenuOptions _ReadBooksMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 7]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 7, "Enter Number between 1 to 7? ");

        for (enBooksMenuOptions o : enBooksMenuOptions.values())
            if (o.getValue() == choice) return o;

        return enBooksMenuOptions.eBackToMainMenu;
    }

    private static void _GoBackToBooksMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Books Menu...");
        new Scanner(System.in).nextLine();
        showBooksMenu();
    }

    private static void _PerformBooksMenuOption(enBooksMenuOptions option) {
        switch(option) {
            case eBooksList:
                _ShowBooksListScreen();
                _GoBackToBooksMenu();
                break;

            case eBorrowBook:
                _ShowBorrowBookScreen();
                _GoBackToBooksMenu();
                break;

            case eFindBook:
                _ShowFindBookScreen();
                _GoBackToBooksMenu();
                break;

            case eMyOverdueBooks:
                _ShowMyOverdueBooksScreen();
                _GoBackToBooksMenu();
                break;

            case eReturnBook:
                _ShowReturnBookScreen();
                _GoBackToBooksMenu();
                break;


            case eBackToMainMenu:
                break;
        }
    }

    public static void showBooksMenu() {
        System.out.print("\033[H\033[2J");
        _DrawScreenHeader("\t\tMain Screen");

        String pad = String.format("%37s", "");

        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tBooks Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Books List.");
        System.out.println(pad + "\t[2] Borrow Book.");
        System.out.println(pad + "\t[3] Find Book.");
        System.out.println(pad + "\t[4] Main Menu.");
        System.out.println(pad + "\t[5] My Overdue Books.");
        System.out.println(pad + "\t[6] Return Book.");

        System.out.println(pad + "===========================================");

        _PerformBooksMenuOption(_ReadBooksMenuOption());
    }
}
