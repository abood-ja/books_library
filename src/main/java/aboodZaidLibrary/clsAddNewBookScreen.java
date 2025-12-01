package aboodZaidLibrary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

public class clsAddNewBookScreen extends clsScreen {

    private static final String SEP = "#//#";
    private static final String BOOKS_FILE = "Books.txt";
    private static void __ensureBooksFile() {
        try {
            Path p = Paths.get(BOOKS_FILE);
            if (Files.notExists(p)) Files.createFile(p);
        } catch (IOException e) {
            System.out.println("⚠️ Cannot create Books file: " + e.getMessage());
        }
    }

    // نفس منطق الفحص المرن المستخدم في الاستعارة
    private static boolean __isbnExists(String isbn) {
        Path p = Paths.get(BOOKS_FILE);
        if (!Files.exists(p)) return false;
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

    private static boolean __appendBook(String title, String author, String isbn) {
        String line = String.join(SEP, title, author, isbn); // نعتمد معيار كتابة واضح
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(BOOKS_FILE), StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("⚠️ Error writing book: " + e.getMessage());
            return false;
        }
    }

    public static void showAddNewBookScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tAdd New Book");

        // التحقق من الصلاحيات: أدمن eAll أو pAddNewBook
        if (clsUserSession.currentUser == null || clsUserSession.currentUser.isEmpty()
                || !clsUserSession.currentUser.checkAccessPermission(clsUser.enPermissions.pAddNewBook)) {
            System.out.println("❌ You do not have permission to add books.");
            return;
        }

        __ensureBooksFile();

        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.print("Enter Title: ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) { System.out.println("❌ Title cannot be empty."); return; }

        System.out.print("Enter Author: ");
        String author = sc.nextLine().trim();
        if (author.isEmpty()) { System.out.println("❌ Author cannot be empty."); return; }

        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine().trim();
        if (isbn.isEmpty()) { System.out.println("❌ ISBN cannot be empty."); return; }

        if (__isbnExists(isbn)) {
            System.out.println("⚠️ A book with this ISBN already exists.");
            return;
        }

        if (__appendBook(title, author, isbn)) {
            System.out.println("✅ Book added successfully.");
            System.out.println("➡️ Saved format: title#//#author#//#isbn");
        } else {
            System.out.println("⚠️ Failed to add the book.");
        }
    }
}
