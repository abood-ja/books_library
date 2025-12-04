package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestClsFindBookScreen {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream outputStream;

    private static final Path booksFile = Paths.get("Books.txt");
    private static final Path backupFile = Paths.get("Books_backup.txt");

    @BeforeEach
    void setUp() throws IOException {
        if (clsUserSession.currentUser == null) {
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");
        }
        // Backup Books.txt if it exists
        if (Files.exists(booksFile)) {
            Files.copy(booksFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore Books.txt from backup
        if (Files.exists(backupFile)) {
            Files.copy(backupFile, booksFile, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(backupFile);
        }

        // Restore system streams
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testPrintBook() throws Exception {
        // Create a dummy book with title "book" and author "abood"
        clsBook book = new clsBook(clsBook.enMode.AddNewMode, "book", "abood", "1234567890");

        // Access private static _PrintBook method using reflection
        Method printBookMethod = clsFindBookScreen.class.getDeclaredMethod("_PrintBook", clsBook.class);
        printBookMethod.setAccessible(true);

        // Invoke the method
        printBookMethod.invoke(null, book);

        // Capture and check output
        String output = outputStream.toString();
        assertTrue(output.contains("Book Title  : book"));
        assertTrue(output.contains("Book Author : abood"));
        assertTrue(output.contains("Book ISPN   : 1234567890"));
    }

    @Test
    void testShowSearchResults_EmptyVector() throws Exception {
        Vector<clsBook> books = new Vector<>();

        Method method = clsFindBookScreen.class.getDeclaredMethod("_ShowSearchResults", Vector.class);
        method.setAccessible(true);

        method.invoke(null, books);

        String output = outputStream.toString();

        assertTrue(output.contains("No books found :-("));
    }

    @Test
    void testShowSearchResults_WithBooks() throws Exception {
        Vector<clsBook> books = new Vector<>();

        clsBook book = new clsBook(clsBook.enMode.AddNewMode, "book", "abood", "1234567890");
        books.add(book);

        Method method = clsFindBookScreen.class.getDeclaredMethod("_ShowSearchResults", Vector.class);
        method.setAccessible(true);

        method.invoke(null, books);

        String output = outputStream.toString();

        assertTrue(output.contains("Book(s) Found :-)"));
        assertTrue(output.contains("Book Title  : book"));
        assertTrue(output.contains("Book Author : abood"));
        assertTrue(output.contains("Book ISPN   : 1234567890"));
    }

    @Test
    void TestSearchByTitle() throws Exception {

        Path booksFile = Paths.get("Books.txt");
        Path backupFile = Paths.get("Books_backup.txt");

        // Backup Books.txt
        if (Files.exists(booksFile)) {
            Files.copy(booksFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty Books.txt and write test line
            Files.write(booksFile, "soft#//#zaid#//#soft1234".getBytes());

            // Create fake input for the test
            String fakeInput = "1\nsoft\n"; // 1 = search by title, then "soft"

            // Assign a Scanner to clsInputValidate for testing
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method
            clsFindBookScreen.showFindBookScreen();

            // Check output
            String output = outputStream.toString();
            assertTrue(output.contains("Book(s) Found :-)"));
            assertTrue(output.contains("Book Title  : soft"));
            assertTrue(output.contains("Book Author : zaid"));
            assertTrue(output.contains("Book ISPN   : soft1234"));

        } finally {
            // Restore Books.txt
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, booksFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner so it won't affect other tests
            clsInputValidate.setTestScanner(null);
        }
    }
    @Test
    void TestSearchByAuthor() throws Exception {

        Path booksFile = Paths.get("Books.txt");
        Path backupFile = Paths.get("Books_backup.txt");

        // Backup Books.txt
        if (Files.exists(booksFile)) {
            Files.copy(booksFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty Books.txt and write test line
            Files.write(booksFile, "soft#//#zaid#//#soft1234".getBytes());

            // Create fake input for the test
            String fakeInput = "2\nzaid\n"; // 2 = search by author, then "zaid"

            // Assign a Scanner to clsInputValidate for testing
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method
            clsFindBookScreen.showFindBookScreen();

            // Check output
            String output = outputStream.toString();
            assertTrue(output.contains("Book(s) Found :-)"));
            assertTrue(output.contains("Book Title  : soft"));
            assertTrue(output.contains("Book Author : zaid"));
            assertTrue(output.contains("Book ISPN   : soft1234"));

        } finally {
            // Restore Books.txt
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, booksFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner
            clsInputValidate.setTestScanner(null);
        }
    }

    @Test
    void TestSearchByISBN() throws Exception {

        Path booksFile = Paths.get("Books.txt");
        Path backupFile = Paths.get("Books_backup.txt");

        // Backup Books.txt
        if (Files.exists(booksFile)) {
            Files.copy(booksFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty Books.txt and write test line
            Files.write(booksFile, "soft#//#zaid#//#soft1234".getBytes());

            // Create fake input for the test
            String fakeInput = "3\nsoft1234\n"; // 3 = search by ISBN, then "soft1234"

            // Assign a Scanner to clsInputValidate for testing
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method
            clsFindBookScreen.showFindBookScreen();

            // Check output
            String output = outputStream.toString();
            assertTrue(output.contains("Book(s) Found :-)"));
            assertTrue(output.contains("Book Title  : soft"));
            assertTrue(output.contains("Book Author : zaid"));
            assertTrue(output.contains("Book ISPN   : soft1234"));

        } finally {
            // Restore Books.txt
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, booksFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner
            clsInputValidate.setTestScanner(null);
        }
    }



}
