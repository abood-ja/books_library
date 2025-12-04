package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
class TestClsDeleteBookScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        if (clsUserSession.currentUser == null) {
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");
        }
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void test_PrintBook() throws Exception {
        // Create a sample book using your constructor
        clsBook book = new clsBook(clsBook.enMode.AddNewMode, "book", "abood", "1234567890");

        // Access the private static method using reflection
        Method printBookMethod = clsDeleteBookScreen.class.getDeclaredMethod("_PrintBook", clsBook.class);
        printBookMethod.setAccessible(true);

        // Invoke the private method
        printBookMethod.invoke(null, book);

        // Capture the output
        String output = outputStream.toString();

        // Assertions to verify the printed output
        assertTrue(output.contains("Book Details:"));
        assertTrue(output.contains("Book Title  : book"));
        assertTrue(output.contains("Book Author : abood"));
        assertTrue(output.contains("Book ISPN   : 1234567890"));
    }

    @Test
    void test_showDeleteBookScreen_NoAccess() {
        // Create a user with no delete permission
        clsUserSession.currentUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "testuser",
                "password",
                0 // no permissions
        );

        // Call the method
        clsDeleteBookScreen.showDeleteBookScreen();

        // Capture output
        String output = outputStream.toString();

        // Debug: print output for inspection
        System.out.println("Captured output: [" + output + "]");

        // Assertion: only fail if actual output contains "Please enter Book ISPN"
        assertFalse(output.contains("Please enter Book ISPN"),
                "Expected no prompt because user has no delete access");
    }



    @Test
    void test_showDeleteBookScreen_WithAccess() throws Exception {
        // 1. Backup the file
        File booksFile = new File("Books.txt");
        File backupFile = new File("Books_backup.txt");
        if (booksFile.exists()) {
            Files.copy(booksFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty the file
            PrintWriter writer = new PrintWriter(booksFile);
            writer.close();

            // 3. Add a test book line
            try (FileWriter fw = new FileWriter(booksFile, true)) {
                fw.write("soft#//#zaid#//#soft1234\n");
            }

            // 4. Create a user with delete permission
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "testuser",
                    "password",
                    clsUser.enPermissions.pDeleteBook.getValue()
            );

            // 5. Use a test Scanner for simulated input
            String simulatedInput = "soft1234\ny\n"; // ISPN line, then confirmation line
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 6. Call the method
            clsDeleteBookScreen.showDeleteBookScreen();

            // 7. Capture output
            String output = outputStream.toString();
            assertTrue(output.contains("Book Details:"));
            assertTrue(output.contains("Book Title  : soft"));
            assertTrue(output.contains("Book Author : zaid"));
            assertTrue(output.contains("Book ISPN   : soft1234"));
            assertTrue(output.contains("Book Deleted Successfully"));

            // 8. Verify the book was deleted
            String fileContent = String.join("\n", Files.readAllLines(booksFile.toPath()));
            assertFalse(fileContent.contains("soft#//#zaid#//#soft1234"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore original file
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), booksFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                backupFile.delete();
            }
        }
    }

    @Test
    void test_showDeleteBookScreen_WithAccess_AnswerNo() throws Exception {
        // 1. Backup the file
        File booksFile = new File("Books.txt");
        File backupFile = new File("Books_backup.txt");
        if (booksFile.exists()) {
            Files.copy(booksFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty the file
            PrintWriter writer = new PrintWriter(booksFile);
            writer.close();

            // 3. Add a test book line
            try (FileWriter fw = new FileWriter(booksFile, true)) {
                fw.write("soft#//#zaid#//#soft1234\n");
            }

            // 4. Create a user with delete permission
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "testuser",
                    "password",
                    clsUser.enPermissions.pDeleteBook.getValue()
            );

            // 5. Use a test Scanner for simulated input (ISPN + 'n')
            String simulatedInput = "soft1234\nn\n"; // ISPN line, then 'n' for no
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 6. Call the method
            clsDeleteBookScreen.showDeleteBookScreen();

            // 7. Capture output
            String output = outputStream.toString();
            assertTrue(output.contains("Book Details:"));
            assertTrue(output.contains("Book Title  : soft"));
            assertTrue(output.contains("Book Author : zaid"));
            assertTrue(output.contains("Book ISPN   : soft1234"));
            assertTrue(output.contains("Are you sure you want to delete this book y/n?"));

            // 8. Verify the book was NOT deleted from the file
            String fileContent = String.join("\n", Files.readAllLines(booksFile.toPath()));
            assertTrue(fileContent.contains("soft#//#zaid#//#soft1234"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore original file
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), booksFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                backupFile.delete();
            }
        }
    }


}
