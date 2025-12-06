package aboodZaidLibrary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestClsUserBooksMenuScreen {
    @Test
    void test___ensureLoansFile_createsFileIfMissing() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt if it exists
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Delete the file to simulate missing file
            Files.deleteIfExists(loansPath);
            assertFalse(Files.exists(loansPath), "Loans.txt should not exist before test");

            // Step 3: Call the private method via reflection
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("__ensureLoansFile");
            method.setAccessible(true);
            method.invoke(null); // static method, pass null

            // Step 4: Verify the file is created
            assertTrue(Files.exists(loansPath), "Loans.txt should be created by __ensureLoansFile");

        } finally {
            // Step 5: Restore the original file if there was a backup
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }

    @AfterEach
    void restoreSystemInOut() {
        System.setIn(System.in);
        clsInputValidate.setTestScanner(null);
    }


    @Test
    void test__ShowBorrowBookScreen_emptyISBN() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");
        Path booksPath = Paths.get("Books.txt");
        Path booksBak = Paths.get("Books.txt.bak");
        Path finesPath = Paths.get("Fines.txt");
        Path finesBak = Paths.get("Fines.txt.bak");

        // Backup files
        if (Files.exists(loansPath)) Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(booksPath)) Files.copy(booksPath, booksBak, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(finesPath)) Files.copy(finesPath, finesBak, StandardCopyOption.REPLACE_EXISTING);

        try {
            // Empty files
            Files.deleteIfExists(loansPath); Files.createFile(loansPath);
            Files.deleteIfExists(booksPath); Files.createFile(booksPath);
            Files.deleteIfExists(finesPath); Files.createFile(finesPath);

            // Login fake user
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "qbr",
                    "password",
                    0
            );

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Provide empty ISBN input
            String inputEmptyISBN = "\n";
            System.setIn(new ByteArrayInputStream(inputEmptyISBN.getBytes()));
            clsInputValidate.setTestScanner(new Scanner(System.in));

            // Call the method
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowBorrowBookScreen");
            method.setAccessible(true);
            method.invoke(null);

            String output = outContent.toString();
            assertTrue(output.contains("❌ ISBN cannot be empty."), "Should detect empty ISBN");

        } finally {
            // Restore files and streams
            if (Files.exists(loansBak)) Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(loansPath);
            if (Files.exists(booksBak)) Files.move(booksBak, booksPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(booksPath);
            if (Files.exists(finesBak)) Files.move(finesBak, finesPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(finesPath);
            System.setOut(System.out);
            System.setIn(System.in);
        }
    }

    @Test
    void test__ShowBorrowBookScreen_nonExistingISBN() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");
        Path booksPath = Paths.get("Books.txt");
        Path booksBak = Paths.get("Books.txt.bak");
        Path finesPath = Paths.get("Fines.txt");
        Path finesBak = Paths.get("Fines.txt.bak");

        // Backup files
        if (Files.exists(loansPath)) Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(booksPath)) Files.copy(booksPath, booksBak, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(finesPath)) Files.copy(finesPath, finesBak, StandardCopyOption.REPLACE_EXISTING);

        try {
            // Empty files
            Files.deleteIfExists(loansPath); Files.createFile(loansPath);
            Files.deleteIfExists(booksPath); Files.createFile(booksPath);
            Files.deleteIfExists(finesPath); Files.createFile(finesPath);

            // Login fake user
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "qbr",
                    "password",
                    0
            );

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Provide non-existing ISBN input
            String inputISBN = "1234\n";
            System.setIn(new ByteArrayInputStream(inputISBN.getBytes()));
            clsInputValidate.setTestScanner(new Scanner(System.in));

            // Call the method
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowBorrowBookScreen");
            method.setAccessible(true);
            method.invoke(null);

            String output = outContent.toString();
            assertTrue(output.contains("❌ Book not found."), "Should detect ISBN that does not exist");

        } finally {
            // Restore files and streams
            if (Files.exists(loansBak)) Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(loansPath);
            if (Files.exists(booksBak)) Files.move(booksBak, booksPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(booksPath);
            if (Files.exists(finesBak)) Files.move(finesBak, finesPath, StandardCopyOption.REPLACE_EXISTING); else Files.deleteIfExists(finesPath);
            System.setOut(System.out);
            System.setIn(System.in);
        }
    }

    @Test
    void test__ShowBorrowBookScreen_userCannotBorrow_dueToActiveLoan() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty the file and write a loan for user 'ahmad' ===
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);
            String line = "1234#//#ahmad#//#2024-10-01#//#2024-10-05#//#false";
            Files.write(loansPath, Arrays.asList(line), StandardOpenOption.APPEND);

            // === Step 3: Login with user 'ahmad' ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "Ahmad",
                    "ahmad@example.com",
                    "1234567890",
                    "ahmad",
                    "password",
                    0
            );

            // === Step 4: Capture System.out ===
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the method
                Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowBorrowBookScreen");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Assert output contains the blocking message ===
            String output = outContent.toString();
            assertTrue(output.contains("❌ Borrowing blocked"),
                    "Should block borrowing due to existing active loan");

        } finally {
            // === Step 6: Restore original file ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }

    @Test
    void test__ShowBorrowBookScreen_successfulBorrow() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");
        Path booksPath = Paths.get("Books.txt");
        Path booksBakPath = Paths.get("Books.txt.bak");

        // === Step 1: Backup the original files ===
        if (Files.exists(loansPath)) Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(booksPath)) Files.copy(booksPath, booksBakPath, StandardCopyOption.REPLACE_EXISTING);

        try {
            // === Step 2: Empty both files ===
            Files.deleteIfExists(loansPath);
            Files.deleteIfExists(booksPath);
            Files.createFile(loansPath);
            Files.createFile(booksPath);

            // === Step 3: Write a book into Books.txt ===
            String bookLine = "qa#//#qa#//#123";
            Files.write(booksPath, Arrays.asList(bookLine), StandardOpenOption.APPEND);

            // === Step 4: Login as a user ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "user1",
                    "password",
                    0
            );

            // === Step 5: Simulate input of ISBN "123" ===
            String input = "123\n";
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            clsInputValidate.setTestScanner(new Scanner(in));

            // === Step 6: Capture output ===
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the private method via reflection
                Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowBorrowBookScreen");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 7: Assert success message is printed ===
            String output = outContent.toString();
            assertTrue(output.contains("✅ Borrowed successfully!"), "Should print successful borrow message");

            // === Step 8: Assert the loan is saved in Loans.txt ===
            List<String> loanLines = Files.readAllLines(loansPath);
            assertEquals(1, loanLines.size(), "Loans.txt should contain one entry");
            assertTrue(loanLines.get(0).contains("123#//#user1"), "Loans.txt should contain the correct ISBN and username");

        } finally {
            // === Step 9: Restore backups ===
            if (Files.exists(loansBakPath)) Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            else Files.deleteIfExists(loansPath);

            if (Files.exists(booksBakPath)) Files.move(booksBakPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
            else Files.deleteIfExists(booksPath);
        }
    }

    @Test
    void test__ShowMyOverdueBooksScreen_noLoansFile() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Delete the Loans.txt file to simulate non-existence ===
            Files.deleteIfExists(loansPath);

            // === Step 3: Set a current user ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "user1",
                    "password",
                    0
            );

            // === Step 4: Capture the output ===
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the private method via reflection
                Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowMyOverdueBooksScreen");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Assert the expected message ===
            String output = outContent.toString();
            assertTrue(output.contains("No loans found."), "Should print 'No loans found.' when Loans.txt does not exist");

        } finally {
            // === Step 6: Restore the original file ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }



    @Test
    void test__ShowMyOverdueBooksScreen_userHasOverdueBook() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty the Loans.txt file and add an overdue loan ===
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            String overdueLine = "1234#//#ahmad#//#2024-10-01#//#2024-10-05#//#false";
            Files.write(loansPath, Arrays.asList(overdueLine), StandardOpenOption.APPEND);

            // === Step 3: Set the current logged-in user ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "ahmad",   // username matches the loan
                    "password",
                    0
            );

            // === Step 4: Capture the output ===
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the private method via reflection
                Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowMyOverdueBooksScreen");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Assert that the overdue book is printed ===
            String output = outContent.toString();
            assertTrue(output.contains("- ISBN: 1234"),
                    "Should list the overdue book for user ahmad");

        } finally {
            // === Step 6: Restore the original Loans.txt ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }



    @Test
    void test_showBooksMenu_displaysMenu() throws Exception {
        // Backup System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Use reflection to temporarily replace _PerformBooksMenuOption with a no-op
            Method performMethod = clsUserBooksMenuScreen.class.getDeclaredMethod(
                    "_PerformBooksMenuOption", clsUserBooksMenuScreen.enBooksMenuOptions.class);
            performMethod.setAccessible(true);

            // Save original method (optional for more advanced mocking if needed)

            // Create a dynamic proxy to do nothing when called
            // For simplicity, here we'll just skip calling it via reflection in the test
            // and call showBooksMenu() without triggering the input-dependent code
            // by temporarily overriding _ReadBooksMenuOption to return a default value

            Method readOptionMethod = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
            readOptionMethod.setAccessible(true);

            // Replace _ReadBooksMenuOption via a lambda to always return eBackToMainMenu
            // (requires more complex bytecode manipulation or just reflection to call showBooksMenu safely)
            // Easier approach: simulate input for a single menu choice + extra "enter" for any subsequent input
            String input = "4\n\n"; // choose Main Menu, then Enter for 'Press any key'
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);

            // Call the menu
            clsUserBooksMenuScreen.showBooksMenu();

            // Assert menu printed expected lines
            String output = outContent.toString();
            assertTrue(output.contains("Books Menu"));
            assertTrue(output.contains("[1] Books List."));
            assertTrue(output.contains("[2] Borrow Book."));
            assertTrue(output.contains("[3] Find Book."));
            assertTrue(output.contains("[4] Main Menu."));
            assertTrue(output.contains("[5] My Overdue Books."));
            assertTrue(output.contains("[6] Return Book."));

        } finally {
            System.setOut(originalOut);
            System.setIn(System.in); // restore original input
        }
    }



    @Test
    void test__ReadBooksMenuOption_eBooksList() throws Exception {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eBooksList, result);
    }

    @Test
    void test__ReadBooksMenuOption_eBorrowBook() throws Exception {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eBorrowBook, result);
    }




    @Test
    void test__ReadBooksMenuOption_eFindBook() throws Exception {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eFindBook, result);
    }

    @Test
    void test__ReadBooksMenuOption_eBackToMainMenu() throws Exception {
        String input = "4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eBackToMainMenu, result);
    }

    @Test
    void test__ReadBooksMenuOption_eMyOverdueBooks() throws Exception {
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eMyOverdueBooks, result);
    }

    @Test
    void test__ReadBooksMenuOption_eReturnBook() throws Exception {
        String input = "6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);

        assertEquals(clsUserBooksMenuScreen.enBooksMenuOptions.eReturnBook, result);
    }




    @Test
    void test___saveLoan_savesLineCorrectly() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt if it exists
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Empty the file
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // Step 3: Call the private method via reflection
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod(
                    "__saveLoan", String.class, String.class, LocalDate.class, LocalDate.class
            );
            method.setAccessible(true);

            String isbn = "mv-v32book2";
            String username = "User2";
            LocalDate borrow = LocalDate.parse("2025-11-02");
            LocalDate due = LocalDate.parse("2025-11-04");

            boolean result = (boolean) method.invoke(null, isbn, username, borrow, due);

            // Verify the method returned true
            assertTrue(result, "Method should return true on successful save");

            // Step 4: Verify the file contains the expected line
            String expectedLine = "mv-v32book2#//#User2#//#2025-11-02#//#2025-11-04#//#false";
            List<String> lines = Files.readAllLines(loansPath);
            assertTrue(lines.contains(expectedLine), "Loans.txt should contain the saved loan line");

        } finally {
            // Step 5: Restore the original file
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }


    @Test
    void test__ShowMyOverdueBooksScreen_emptyLoansFile() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty the Loans.txt file ===
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // === Step 3: Set a current user ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "user1",
                    "password",
                    0
            );

            // === Step 4: Capture the output ===
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the private method via reflection
                Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("_ShowMyOverdueBooksScreen");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Assert the expected message ===
            String output = outContent.toString();
            assertTrue(output.contains("You have no overdue items."),
                    "Should print 'You have no overdue items.' when Loans.txt is empty");

        } finally {
            // === Step 6: Restore the original file ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }





    @Test
    void test___ensureLoansFile_fileAlreadyExists() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Ensure Loans.txt exists and is empty
            if (!Files.exists(loansPath)) {
                Files.createFile(loansPath);
            }
            Files.write(loansPath, new byte[0]); // empty the file

            // Step 3: Call the private method via reflection
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("__ensureLoansFile");
            method.setAccessible(true);
            method.invoke(null);

            // Step 4: Verify the file still exists (content remains empty)
            assertTrue(Files.exists(loansPath), "Loans.txt should still exist after __ensureLoansFile");
            assertEquals(0, Files.readAllBytes(loansPath).length, "Loans.txt should remain empty");

        } finally {
            // Step 5: Restore the original file
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }

    @Test
    void test___bookExistsByIsbn_fileDoesNotExist_returnsFalse() throws Exception {
        Path booksPath = Paths.get("Books.txt");
        Path booksBakPath = Paths.get("Books.txt.bak");

        // Step 1: Backup the original Books.txt if it exists
        if (Files.exists(booksPath)) {
            Files.copy(booksPath, booksBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Delete the file
            Files.deleteIfExists(booksPath);
            assertFalse(Files.exists(booksPath), "Books.txt should not exist before test");

            // Step 3: Call the private method via reflection
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("__bookExistsByIsbn", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "12345");

            // Step 4: Verify it returns false
            assertFalse(result, "Should return false if Books.txt does not exist");

        } finally {
            // Step 5: Restore backup if exists
            if (Files.exists(booksBakPath)) {
                Files.move(booksBakPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Test
    void test___bookExistsByIsbn_fileEmpty_returnsFalse() throws Exception {
        Path booksPath = Paths.get("Books.txt");
        Path booksBakPath = Paths.get("Books.txt.bak");

        // Backup original
        if (Files.exists(booksPath)) {
            Files.copy(booksPath, booksBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file
            Files.deleteIfExists(booksPath);
            Files.createFile(booksPath);

            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("__bookExistsByIsbn", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "12345");

            assertFalse(result, "Should return false if Books.txt is empty");

        } finally {
            if (Files.exists(booksBakPath)) {
                Files.move(booksBakPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(booksPath);
            }
        }
    }

    @Test
    void test___bookExistsByIsbn_bookExists_returnsTrue() throws Exception {
        Path booksPath = Paths.get("Books.txt");
        Path booksBakPath = Paths.get("Books.txt.bak");

        // Backup original
        if (Files.exists(booksPath)) {
            Files.copy(booksPath, booksBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty and write a book
            Files.deleteIfExists(booksPath);
            Files.createFile(booksPath);
            String line = "wq#//#wq#//#11";
            Files.write(booksPath, Arrays.asList(line));

            // Call method
            Method method = clsUserBooksMenuScreen.class.getDeclaredMethod("__bookExistsByIsbn", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "11");

            assertTrue(result, "Should return true because the book with ISBN '11' exists");

        } finally {
            if (Files.exists(booksBakPath)) {
                Files.move(booksBakPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(booksPath);
            }
        }
    }


}
