package aboodZaidLibrary;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsPayFineScreen extends clsScreen {

    private static final String SEP = "#//#";
    private static final String LOANS_FILE = "Loans.txt";

    public static void showPayFineScreen() {

        String pad = String.format("%37s", "");
        System.out.print("\033[H\033[2J");
        _DrawScreenHeader("Pay Fines");

        String username = clsUserSession.currentUser.getUserName().trim().toLowerCase();

        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) {
            System.out.println(pad + "No loans found.");
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(f);
        } catch (Exception e) {
            System.out.println(pad + "Error reading loans: " + e.getMessage());
            return;
        }

        LocalDate today = LocalDate.now();
        long totalFine = 0;

        // ============================
        //       Calculate Fine
        // ============================
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] p = line.split(Pattern.quote(SEP), -1);

            // BOOK → 5 parts
            if (p.length == 5) {
                String u = p[1].trim().toLowerCase();
                LocalDate due = LocalDate.parse(p[3].trim());
                boolean returned = Boolean.parseBoolean(p[4].trim());

                if (u.equals(username) && !returned && today.isAfter(due)) {
                    long days = ChronoUnit.DAYS.between(due, today);
                    totalFine += days;
                }
            }

            // CD → 6 parts
            else if (p.length == 6) {
                String u = p[2].trim().toLowerCase();
                LocalDate due = LocalDate.parse(p[4].trim());
                boolean returned = Boolean.parseBoolean(p[5].trim());

                if (u.equals(username) && !returned && today.isAfter(due)) {
                    long days = ChronoUnit.DAYS.between(due, today);
                    totalFine += days;
                }
            }
        }

        if (totalFine == 0) {
            System.out.println(pad + "You have no fines to pay. 👍");
            return;
        }

        System.out.println(pad + "❗ Unpaid Fines: " + totalFine + " NIS");
        System.out.print(pad + "Pay all fines now? [y/n]: ");
        char ans = clsInputValidate.readChar();

        if (ans != 'y' && ans != 'Y') {
            System.out.println(pad + "Payment canceled.");
            return;
        }

        // ============================
        //        Clear All Fines
        // ============================
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] p = line.split(Pattern.quote(SEP), -1);

            // BOOK
            if (p.length == 5) {
                String u = p[1].trim().toLowerCase();
                boolean returned = Boolean.parseBoolean(p[4]);

                if (u.equals(username) && !returned) {
                    p[4] = "true";
                    lines.set(i, String.join(SEP, p));
                }
            }

            // CD
            else if (p.length == 6) {
                String u = p[2].trim().toLowerCase();
                boolean returned = Boolean.parseBoolean(p[5]);

                if (u.equals(username) && !returned) {
                    p[5] = "true";
                    lines.set(i, String.join(SEP, p));
                }
            }
        }

        try {
            Files.write(f, lines);
            System.out.println(pad + "✔ All fines paid successfully!");
        } catch (Exception e) {
            System.out.println(pad + "Error saving changes: " + e.getMessage());
        }
    }
}
