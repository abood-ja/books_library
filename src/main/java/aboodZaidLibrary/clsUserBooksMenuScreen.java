package aboodZaidLibrary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsUserBooksMenuScreen extends clsScreen {

    // ===== إعدادات الملفات والفاصل =====
    private static final String SEP = "#//#";
    private static final String BOOKS_FILE = "Books.txt";
    private static final String LOANS_FILE = "Loans.txt";

    // ===== إنشاء ملف Loans.txt إن لم يوجد =====
    private static void __ensureLoansFile() {
        try {
            Path p = Paths.get(LOANS_FILE);
            if (Files.notExists(p)) Files.createFile(p);
        } catch (IOException e) {
            System.out.println("⚠️ Cannot create Loans file: " + e.getMessage());
        }
    }

    // ===== يتحقق من وجود الكتاب بأي ترتيب أعمدة =====
    // يدعم صيغ شائعة:
    // 1) title#//#author#//#isbn
    // 2) isbn#//#title#//#author
    // 3) title#//#isbn#//#author
    private static boolean __bookExistsByIsbn(String isbn) {
        Path p = Paths.get(BOOKS_FILE);
        if (!Files.exists(p)) {
            System.out.println("⚠️ " + BOOKS_FILE + " not found.");
            return false;
        }
        try {
            for (String line : Files.readAllLines(p)) {
                if (line == null) continue;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(Pattern.quote(SEP), -1);
                for (String cell : cols) {
                    if (cell != null && cell.trim().equalsIgnoreCase(isbn)) return true;
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading " + BOOKS_FILE + ": " + e.getMessage());
        }
        return false;
    }

    // ===== يتحقق إذا الكتاب مستعار حالياً (returned=false) =====
    private static boolean __isCurrentlyBorrowed(String isbn) {
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) return false;
        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP), -1);
                if (p.length >= 5) {
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());
                    if (p[0].trim().equalsIgnoreCase(isbn) && !returned) return true;
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading " + LOANS_FILE + ": " + e.getMessage());
        }
        return false;
    }

    // ===== يحفظ عملية الاستعارة: isbn#//#username#//#borrow#//#due#//#false =====
    private static boolean __saveLoan(String isbn, String username, LocalDate borrow, LocalDate due) {
        String line = String.join(SEP, isbn, username, borrow.toString(), due.toString(), "false");
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(LOANS_FILE), StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("⚠️ Error writing loan: " + e.getMessage());
            return false;
        }
    }

    // ===== شاشة الاستعارة =====
    private static void _ShowBorrowBookScreen() {
        String pad = String.format("%37s", "");
        System.out.flush();
        _DrawScreenHeader("\t\tBorrow Book");

        __ensureLoansFile();

        // اسم المستخدم من الجلسة إن وُجد، وإلا نطلبه
        String username;
        if (clsUserSession.currentUser != null
                && !clsUserSession.currentUser.isEmpty()
                && clsUserSession.currentUser.getUserName() != null
                && !clsUserSession.currentUser.getUserName().trim().isEmpty()) {
            username = clsUserSession.currentUser.getUserName().trim();
            System.out.println(pad + "Active user: " + username);
        } else {
            System.out.print(pad + "Enter your username: ");
            username = new java.util.Scanner(System.in).nextLine().trim();
            if (username.isEmpty()) {
                System.out.println(pad + "❌ Username cannot be empty.");
                return;
            }
        }

        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print(pad + "Enter ISBN to borrow: ");
        String isbn = sc.nextLine().trim();
        if (isbn.isEmpty()) {
            System.out.println(pad + "❌ ISBN cannot be empty.");
            return;
        }

        if (!__bookExistsByIsbn(isbn)) {
            System.out.println(pad + "❌ Book not found with this ISBN in " + BOOKS_FILE);
            return;
        }

        if (__isCurrentlyBorrowed(isbn)) {
            System.out.println(pad + "❌ Book is already borrowed.");
            return;
        }

        LocalDate borrow = LocalDate.now();
        LocalDate due = borrow.plusDays(28); // المدة الافتراضية

        if (__saveLoan(isbn, username, borrow, due)) {
            System.out.println(pad + "✅ Borrowed successfully. User: " + username + " | Due date: " + due);
        } else {
            System.out.println(pad + "⚠️ Could not save the loan.");
        }
    }

    // ===== شاشة "كتبي المتأخرة" =====
    private static void _ShowMyOverdueBooksScreen() {
        String pad = String.format("%37s", "");
        System.out.flush();
        _DrawScreenHeader("\t\tMy Overdue Books");

        // اسم المستخدم من الجلسة إن وُجد، وإلا نطلبه
        String username;
        if (clsUserSession.currentUser != null
                && !clsUserSession.currentUser.isEmpty()
                && clsUserSession.currentUser.getUserName() != null
                && !clsUserSession.currentUser.getUserName().trim().isEmpty()) {
            username = clsUserSession.currentUser.getUserName().trim();
            System.out.println(pad + "Active user: " + username);
        } else {
            System.out.print(pad + "Enter your username: ");
            username = new java.util.Scanner(System.in).nextLine().trim();
            if (username.isEmpty()) {
                System.out.println(pad + "❌ Username cannot be empty.");
                return;
            }
        }
        String usernameKey = username.toLowerCase();

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
                String[] p = line.split(Pattern.quote(SEP), -1);
                // الصيغة: isbn#//#username#//#borrow#//#due#//#returned
                if (p.length >= 5) {
                    String isbn = p[0].trim();
                    String u = p[1].trim();
                    LocalDate due = LocalDate.parse(p[3].trim());
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (!returned && today.isAfter(due) && u.equalsIgnoreCase(usernameKey)) {
                        long days = ChronoUnit.DAYS.between(due, today);
                        System.out.println(pad + "- ISBN: " + isbn + " | Due: " + due + " | Overdue: " + days + " day(s)");
                        any = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(pad + "⚠️ Error reading " + LOANS_FILE + ": " + e.getMessage());
            return;
        }

        if (!any) {
            System.out.println(pad + "You have no overdue items. 👍");
        }
    }

    // ===== شاشة إرجاع كتاب =====
    private static void _ShowReturnBookScreen() {
        String pad = String.format("%37s", "");
        System.out.flush();
        _DrawScreenHeader("\t\tReturn Book");

        // نستخدم المستخدم الحالي إن أمكن
        String username;
        if (clsUserSession.currentUser != null
                && !clsUserSession.currentUser.isEmpty()
                && clsUserSession.currentUser.getUserName() != null
                && !clsUserSession.currentUser.getUserName().trim().isEmpty()) {
            username = clsUserSession.currentUser.getUserName().trim().toLowerCase();
            System.out.println(pad + "Active user: " + username);
        } else {
            System.out.print(pad + "Enter your username: ");
            Scanner sc = new Scanner(System.in);
            username = sc.nextLine().trim().toLowerCase();
            if (username.isEmpty()) {
                System.out.println(pad + "❌ Username cannot be empty.");
                return;
            }
        }

        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) {
            System.out.println(pad + "No loans found.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(f);
            List<Integer> userLoans = new ArrayList<>();
            int index = 1;

            System.out.println(pad + "Your borrowed (not returned) books:");
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP), -1);
                if (p.length >= 5) {
                    String isbn = p[0].trim();
                    String u = p[1].trim().toLowerCase();
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());
                    if (u.equals(username) && !returned) {
                        System.out.println(pad + index + ". ISBN: " + isbn + " | Due: " + p[3]);
                        userLoans.add(i);
                        index++;
                    }
                }
            }

            if (userLoans.isEmpty()) {
                System.out.println(pad + "You have no active borrowed books.");
                return;
            }

            System.out.print(pad + "Enter the number of the book to return: ");
            int choice = clsInputValidate.readIntNumberBetween(1, userLoans.size(), "Enter a valid number: ");

            int lineIndex = userLoans.get(choice - 1);
            String[] parts = lines.get(lineIndex).split(Pattern.quote(SEP), -1);
            if (parts.length >= 5) {
                parts[4] = "true"; // تم الإرجاع
                lines.set(lineIndex, String.join(SEP, parts));
                Files.write(f, lines);
                System.out.println(pad + "✅ Book returned successfully!");
            }
        } catch (IOException e) {
            System.out.println(pad + "⚠️ Error updating " + LOANS_FILE + ": " + e.getMessage());
        }
    }

    // ===== باقي الشاشات =====
    private static void _ShowBooksListScreen() {
        clsBooksListScreen.showBooksListScreen();
    }

    private static void _ShowFindBookScreen() {
        clsFindBookScreen.showFindBookScreen();
    }

    // ===== القائمة =====
    private enum enBooksMenuOptions {
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
        System.out.print(pad + "Choose what do you want to do? [1 to 6]? ");
        int choice = clsInputValidate.readIntNumberBetween(1, 6, "Enter Number between 1 to 6? ");
        for (enBooksMenuOptions o : enBooksMenuOptions.values())
            if (o.getValue() == choice) return o;
        return enBooksMenuOptions.eBackToMainMenu;
    }

    private static void _GoBackToBooksMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Books Menu...");
        new java.util.Scanner(System.in).nextLine();
        showBooksMenu();
    }

    private static void _PerformBooksMenuOption(enBooksMenuOptions option) {
        switch(option) {
            case eBooksList:
                System.out.print("\033[H\033[2J");
                _ShowBooksListScreen();
                _GoBackToBooksMenu();
                break;
            case eBorrowBook:
                System.out.print("\033[H\033[2J");
                _ShowBorrowBookScreen();
                _GoBackToBooksMenu();
                break;
            case eFindBook:
                System.out.print("\033[H\033[2J");
                _ShowFindBookScreen();
                _GoBackToBooksMenu();
                break;
            case eMyOverdueBooks:
                System.out.print("\033[H\033[2J");
                _ShowMyOverdueBooksScreen();
                _GoBackToBooksMenu();
                break;
            case eReturnBook:
                System.out.print("\033[H\033[2J");
                _ShowReturnBookScreen();
                _GoBackToBooksMenu();
                break;
            case eBackToMainMenu:
                break;
        }
    }

    public static void showBooksMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
