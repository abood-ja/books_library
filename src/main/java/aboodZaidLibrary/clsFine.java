package aboodZaidLibrary;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsFine {

    private static final String SEP = "#//#";
    private static final String LOANS_FILE = "Loans.txt";
    private static final String FINES_FILE = "Fines.txt";

    // ===== Fine Rules =====
    private static final int BOOK_FINE_PER_DAY = 1;
    private static final int CD_FINE_PER_DAY   = 2;

    public static class FineRow {
        public String username;
        public String mediaID;
        public String mediaType;   // BOOK or CD
        public long days;
        public long amount;
    }

    private static void ensureFinesFile() {
        try {
            Path p = Paths.get(FINES_FILE);
            if (!Files.exists(p)) Files.createFile(p);
        } catch (Exception ignored) {}
    }

    // ===== Read all unpaid fines =====
    public static List<FineRow> getUserUnpaidFines(String username) {
        username = username.toLowerCase();
        List<FineRow> list = new ArrayList<>();

        Path p = Paths.get(FINES_FILE);
        if (!Files.exists(p)) return list;

        try {
            for (String line : Files.readAllLines(p)) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(Pattern.quote(SEP), -1);
                if (f.length >= 5) {
                    String u = f[0].trim().toLowerCase();
                    String returned = f[4].trim();

                    // unpaid only
                    if (u.equals(username) && returned.equals("false")) {
                        FineRow fr = new FineRow();
                        fr.username = u;
                        fr.mediaID  = f[1].trim();
                        fr.mediaType= f[2].trim();
                        fr.days     = Long.parseLong(f[3].trim());
                        fr.amount   = fr.mediaType.equals("CD") ?
                                fr.days * CD_FINE_PER_DAY :
                                fr.days * BOOK_FINE_PER_DAY;

                        list.add(fr);
                    }
                }
            }
        } catch (Exception ignored) {}

        return list;
    }

    // ===== Total unpaid fine amount =====
    public static long getTotalUserFines(String username) {
        long sum = 0;
        for (FineRow fr : getUserUnpaidFines(username))
            sum += fr.amount;
        return sum;
    }

    // ===== Check if user has unpaid fines =====
    public static boolean hasUnpaidFines(String username) {
        return getTotalUserFines(username) > 0;
    }

    // ===== Generate fines from overdue loans =====
    public static void generateFinesFromOverdueLoans() {
        ensureFinesFile();
        Path p = Paths.get(LOANS_FILE);
        if (!Files.exists(p)) return;

        LocalDate today = LocalDate.now();

        try {
            List<String> lines = Files.readAllLines(p);
            for (String l : lines) {
                if (l.trim().isEmpty()) continue;
                String[] a = l.split(Pattern.quote(SEP), -1);

                if (a.length >= 5) {
                    String isbn  = a[0].trim();
                    String user  = a[1].trim().toLowerCase();
                    LocalDate due = LocalDate.parse(a[3].trim());
                    boolean returned = Boolean.parseBoolean(a[4].trim());

                    if (!returned && today.isAfter(due)) {
                        long days = ChronoUnit.DAYS.between(due, today);
                        String type = isbn.startsWith("CD-") ? "CD" : "BOOK";

                        String line = String.join(SEP,
                                user,
                                isbn,
                                type,
                                Long.toString(days),
                                "false"     // unpaid
                        );

                        Files.write(Paths.get(FINES_FILE),
                                Arrays.asList(line),
                                StandardOpenOption.APPEND);
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    // ===== Mark all user fines as paid =====
    public static void payAllUserFines(String username) {
        username = username.toLowerCase();
        ensureFinesFile();

        try {
            List<String> lines = Files.readAllLines(Paths.get(FINES_FILE));
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] f = line.split(Pattern.quote(SEP), -1);
                if (f.length >= 5) {
                    String u = f[0].trim().toLowerCase();
                    if (u.equals(username)) {
                        f[4] = "true"; // mark paid
                    }
                    newLines.add(String.join(SEP, f));
                }
            }

            Files.write(Paths.get(FINES_FILE), newLines);
        } catch (Exception ignored) {}
    }
}
