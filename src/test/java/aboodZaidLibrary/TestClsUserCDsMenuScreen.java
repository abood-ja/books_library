package aboodZaidLibrary;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class TestClsUserCDsMenuScreen {
    private Object invokeReadCDMenuOption(String input) throws Exception {
        Scanner testScanner = new Scanner(input);
        clsInputValidate.setTestScanner(testScanner);

        Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_ReadCDMenuOption");
        method.setAccessible(true);

        return method.invoke(new clsUserCDsMenuScreen());
    }


    private static final String LOANS_FILE = "Loans.txt"; // same as in your class

    @Test
    void test___ensureLoansFile_createsFileIfMissing() throws Exception {
        Path path = Paths.get(LOANS_FILE);
        Path backupPath = Paths.get(LOANS_FILE + ".bak");

        // 1️⃣ Backup the original file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2️⃣ Delete the file to simulate missing file
            Files.deleteIfExists(path);
            assertFalse(Files.exists(path), "File should be deleted before test");

            // 3️⃣ Call the private method via reflection
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("__ensureLoansFile");
            method.setAccessible(true);
            method.invoke(null); // static method, pass null

            // 4️⃣ Verify the file was created
            assertTrue(Files.exists(path), "File should be created by __ensureLoansFile");

        } finally {
            // 5️⃣ Restore the original file if there was a backup
            if (Files.exists(backupPath)) {
                Files.move(backupPath, path, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Or delete the test-created file if no backup
                Files.deleteIfExists(path);
            }
        }
    }

    @Test
    void test__BorrowCD_whenUserNotLoggedIn_printsErrorAndReturns() throws Exception {
        // 1️⃣ Set a "dummy" empty user
        clsUserSession.currentUser = clsUser.find("", "");

        // 2️⃣ Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // 3️⃣ Call the private method via reflection
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_BorrowCD");
            method.setAccessible(true);
            method.invoke(null); // static method

            // 4️⃣ Verify printed message
            String output = outContent.toString();
            assertTrue(output.contains("❌ You must be logged in."),
                    "Should print message that user must be logged in");

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void test__BorrowCD_cdDoesNotExist_printsError() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans.txt.bak");

        // 1️⃣ Backup the original Loans.txt
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2️⃣ Make the Loans.txt file empty
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // 3️⃣ Create a fictional current user
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

            // 4️⃣ Inject test Scanner to provide a CD ID that does not exist
            String nonExistentCD = "100214312341243124123";
            clsInputValidate.setTestScanner(new Scanner(nonExistentCD + "\n"));

            // 5️⃣ Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call the private method via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_BorrowCD");
                method.setAccessible(true);
                method.invoke(null); // static method

                // 6️⃣ Verify printed message
                String output = outContent.toString();
                assertTrue(output.contains("❌ CD not found."),
                        "Should print message that CD does not exist");

            } finally {
                System.setOut(originalOut);
            }

        } finally {
            // 7️⃣ Restore the original Loans.txt
            if (Files.exists(backupPath)) {
                Files.move(backupPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }

    @Test
    void test___userHasOverdueCDs_fileDoesNotExist_returnsFalse() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2 & 3: Delete the file to simulate it missing ===
            Files.deleteIfExists(loansPath);
            assertFalse(Files.exists(loansPath), "Loans.txt should not exist for this test");

            // === Step 4: Call the private method via reflection ===
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("__userHasOverdueCDs", String.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "anyuser");

            // === Step 5: Assert it returns false ===
            assertFalse(result, "Should return false if Loans.txt does not exist");

        } finally {
            // === Step 6: Restore the original Loans.txt file ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Test
    void test__ReturnCD_userNotLoggedIn_printsError() throws Exception {
        // === Step 1: Set current user as empty ===
        clsUserSession.currentUser = clsUser.find("", "");

        // === Step 2: Capture System.out ===
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // === Step 3: Call the private method via reflection ===
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_ReturnCD");
            method.setAccessible(true);
            method.invoke(null); // static method

            // === Step 4: Verify output contains the correct message ===
            String output = outContent.toString();
            assertTrue(output.contains("❌ Login required."), "Should print message that login is required");

        } finally {
            // === Step 5: Restore System.out ===
            System.setOut(originalOut);
        }
    }


    @Test
    void test__MyOverdueCDs_userNotLoggedIn_printsLoginRequired() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt if it exists
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Delete the Loans.txt file
            Files.deleteIfExists(loansPath);

            // Step 3: Set current user to empty
            clsUserSession.currentUser = clsUser.find("", "");

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call _MyOverdueCDs via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_MyOverdueCDs");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // Step 4: Verify printed message
            String output = outContent.toString();
            assertTrue(output.contains("❌ Login required."),
                    "Should print login required message when user is empty");

        } finally {
            // Step 5: Restore the original Loans.txt
            if (Files.exists(loansBak)) {
                Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }



    @Test
    void test__ReturnCD_loansFileEmpty_printsNoActiveLoans() throws Exception {
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

            // === Step 3: Set a logged-in user ===
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

            try {
                // === Step 4: Call the private method via reflection ===
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_ReturnCD");
                method.setAccessible(true);
                method.invoke(null); // static method
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Verify output ===
            String output = outContent.toString();
            assertTrue(output.contains("No active borrowed CDs."),
                    "Should print message that no active borrowed CDs exist");

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
    void test__ReturnCD_oneActiveLoan_returnsSuccessfully() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty the Loans.txt file and add one active loan ===
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // Add one borrowed CD (not returned)
            String loanLine = "CD#//#1#//#User2#//#2025-11-06#//#2025-12-04#//#false";
            Files.write(loansPath, Arrays.asList(loanLine), StandardOpenOption.APPEND);

            // === Step 3: Set the current logged-in user ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "User2",
                    "password",
                    0
            );

            // Simulate user input choosing the first CD (index 1)
            String input = "1\n";
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            clsInputValidate.setTestScanner(new Scanner(in));

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // === Step 4: Call the private method via reflection ===
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_ReturnCD");
                method.setAccessible(true);
                method.invoke(null); // static method
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Verify output ===
            String output = outContent.toString();
            assertTrue(output.contains("✅ CD returned successfully!"),
                    "Should print message that CD was returned successfully");

            // Optionally: verify that the Loans.txt was updated correctly
            String updatedLine = Files.readAllLines(loansPath).get(0);
            assertTrue(updatedLine.endsWith("true"), "Loan should be marked as returned");

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
    void test__MyOverdueCDs_loansFileEmpty_printsNoOverdueCDs() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt if it exists
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Empty the Loans.txt file
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // Step 3: Set a dummy logged-in user
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "123",
                    "dummyUser",
                    "password",
                    0
            );

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call _MyOverdueCDs via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_MyOverdueCDs");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // Step 4: Verify printed message
            String output = outContent.toString();
            assertTrue(output.contains("No overdue CDs"),
                    "Should print 'No overdue CDs 👍' when Loans.txt is empty");

        } finally {
            // Step 5: Restore the original Loans.txt
            if (Files.exists(loansBak)) {
                Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }


    @Test
    void test__MyOverdueCDs_userHasOverdueCDs_printsOverdue() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");

        // Step 1: Backup the original Loans.txt if it exists
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Step 2: Empty the Loans.txt file and write an overdue loan
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);
            String overdueLine = "CD#//#mv-v32book2#//#User2#//#2025-11-02#//#2025-11-04#//#false";
            Files.write(loansPath, Arrays.asList(overdueLine), StandardOpenOption.APPEND);

            // Step 3: Set the current logged-in user
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "User2",  // username matches the loan
                    "password",
                    0
            );

            // Step 4: Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call _MyOverdueCDs via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_MyOverdueCDs");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // Step 5: Verify output contains overdue CD information
            String output = outContent.toString();
            assertTrue(output.contains("CD ID: mv-v32book2"),
                    "Should list the overdue CD for User2");

        } finally {
            // Step 6: Restore the original Loans.txt
            if (Files.exists(loansBak)) {
                Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }



    @Test
    void test_showCDsMenu_displaysMenuAndReadsOption() throws Exception {
        // Step 1: Simulate user input for menu option (e.g., "6" for Back)
        String input = "6\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        clsInputValidate.setTestScanner(new Scanner(in));

        // Step 2: Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Step 3: Call showCDsMenu
            clsUserCDsMenuScreen.showCDsMenu();
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }

        // Step 4: Verify the menu text is printed
        String output = outContent.toString();
        assertTrue(output.contains("[1] CDs List"));
        assertTrue(output.contains("[2] Borrow CD"));
        assertTrue(output.contains("[3] Return CD"));
        assertTrue(output.contains("[4] My Overdue CDs"));
        assertTrue(output.contains("[5] Find CD"));
        assertTrue(output.contains("[6] Back"));
    }




    @Test
    void test___userHasOverdueCDs_emptyFile_returnsFalse() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBakPath = Paths.get("Loans.txt.bak");

        // === Step 1: Backup the original Loans.txt if it exists ===
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBakPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty the file ===
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);
            assertTrue(Files.exists(loansPath), "Loans.txt should exist but be empty");

            // === Step 3: Call the private method via reflection ===
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("__userHasOverdueCDs", String.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "anyuser");

            // === Step 4: Assert it returns false ===
            assertFalse(result, "Should return false if Loans.txt is empty (no loans)");

        } finally {
            // === Step 5: Restore the original Loans.txt file ===
            if (Files.exists(loansBakPath)) {
                Files.move(loansBakPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }

    @Test
    void test__BorrowCD_whenCDAvailable_borrowsSuccessfully() throws Exception {
        // === Step 1: Backup files ===
        Path loans = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");

        Path cds = Paths.get("CDs.txt");
        Path cdsBak = Paths.get("CDs.txt.bak");

        if (Files.exists(loans)) {
            Files.copy(loans, loansBak, StandardCopyOption.REPLACE_EXISTING);
        }
        if (Files.exists(cds)) {
            Files.copy(cds, cdsBak, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // === Step 2: Empty both files ===
            Files.deleteIfExists(loans);
            Files.deleteIfExists(cds);
            Files.createFile(loans);
            Files.createFile(cds);

            // === Step 3: Write CD with id 12 into CDs.txt ===
            String cdLine = "eewq#//#ewqe#//#12";
            Files.write(cds, Arrays.asList(cdLine));

            // === Step 4: Prepare environment ===
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "123",
                    "qbr",
                    "pass",
                    0
            );

            // Simulate input "12"
            String input = "12\n";
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            System.setIn(in);
            clsInputValidate.setTestScanner(new Scanner(in));

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Call _BorrowCD via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_BorrowCD");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // === Step 5: Assert printed message ===
            String output = outContent.toString();
            assertTrue(output.contains("✅ CD Borrowed Successfully!"),
                    "Should print success message when CD is borrowed");

            // === Step 6: Assert Loans.txt has the correct line ===
            String loanLine = Files.readAllLines(loans).get(0);
            assertTrue(loanLine.contains("CD#//#12#//#qbr"), "Loans.txt should contain the borrowed CD info");

        } finally {
            // === Step 7: Restore backups ===
            if (Files.exists(loansBak)) {
                Files.move(loansBak, loans, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loans);
            }

            if (Files.exists(cdsBak)) {
                Files.move(cdsBak, cds, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(cds);
            }
        }
    }

    @Test
    void test__BorrowCD_cdAlreadyBorrowed_printsError() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path loansBak = Paths.get("Loans.txt.bak");

        Path cdsPath = Paths.get("CDs.txt");
        Path cdsBak = Paths.get("CDs.txt.bak");

        // Step 1: Backup original files
        if (Files.exists(loansPath)) Files.copy(loansPath, loansBak, StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(cdsPath)) Files.copy(cdsPath, cdsBak, StandardCopyOption.REPLACE_EXISTING);

        try {
            // Step 2: Empty the files
            Files.deleteIfExists(loansPath);
            Files.deleteIfExists(cdsPath);
            Files.createFile(loansPath);
            Files.createFile(cdsPath);

            // Step 3: Write a borrowed CD into Loans.txt
            String borrowedLine = "CD#//#1#//#User2#//#2025-11-06#//#2025-12-04#//#false";
            Files.write(loansPath, Arrays.asList(borrowedLine));

            // Step 4: Write the CD into CDs.txt
            String cdLine = "afasdf#//#fff#//#1";
            Files.write(cdsPath, Arrays.asList(cdLine));

            // Step 5: Set logged-in user
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "User3",  // username matches loan
                    "password",
                    0
            );

            // Simulate input for CD ID
            String input = "1\n";
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            clsInputValidate.setTestScanner(new Scanner(in));

            // Capture output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // Step 6: Call _BorrowCD via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_BorrowCD");
                method.setAccessible(true);
                method.invoke(null);
            } finally {
                System.setOut(originalOut);
            }

            // Step 7: Verify output
            String output = outContent.toString();
            assertTrue(output.contains("❌ CD is already borrowed."),
                    "Should detect that CD 1 is already borrowed");

        } finally {
            // Step 8: Restore original files
            if (Files.exists(loansBak)) Files.move(loansBak, loansPath, StandardCopyOption.REPLACE_EXISTING);
            else Files.deleteIfExists(loansPath);

            if (Files.exists(cdsBak)) Files.move(cdsBak, cdsPath, StandardCopyOption.REPLACE_EXISTING);
            else Files.deleteIfExists(cdsPath);
        }
    }





    @Test
    void test___ensureLoansFile_fileAlreadyExists() throws Exception {
        Path path = Paths.get(LOANS_FILE);
        Path backupPath = Paths.get(LOANS_FILE + ".bak");

        // Backup the original file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Ensure the file exists with some content
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, "Test content".getBytes()); // <-- Java 8 compatible

            // Call the private method via reflection
            Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("__ensureLoansFile");
            method.setAccessible(true);
            method.invoke(null); // static method

            // Verify the file still exists
            assertTrue(Files.exists(path), "File should still exist");

            // Verify the content is unchanged
            String content = new String(Files.readAllBytes(path));
            assertEquals("Test content", content, "File content should remain unchanged");

        } finally {
            // Restore the original file if there was a backup
            if (Files.exists(backupPath)) {
                Files.move(backupPath, path, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(path);
            }
        }
    }



    @Test
    void test__BorrowCD_userHasOverdueCDs_printsError() throws Exception {
        Path loansPath = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans.txt.bak");

        // 1️⃣ Backup the original Loans.txt
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2️⃣ Empty the file and create it
            Files.deleteIfExists(loansPath);
            Files.createFile(loansPath);

            // 3️⃣ Write a loan that makes user "qbr" appear to have overdue CD
            // Use a date in the past to guarantee it's overdue
            String overdueLine = "CD#//#1#//#qbr#//#2025-01-01#//#2025-01-08#//#false";
            Files.write(loansPath, Arrays.asList(overdueLine));

            // 4️⃣ Create a fictional current user with username "qbr"
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "qbr",      // username must match the loan
                    "password",
                    0           // no permissions
            );

            // 5️⃣ Inject test Scanner to avoid blocking
            clsInputValidate.setTestScanner(new Scanner("CD1\n"));

            // 6️⃣ Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            try {
                // 7️⃣ Call the private method via reflection
                Method method = clsUserCDsMenuScreen.class.getDeclaredMethod("_BorrowCD");
                method.setAccessible(true);
                method.invoke(null); // static method

                // 8️⃣ Verify printed message
                String output = outContent.toString();
                assertTrue(output.contains("❌ You cannot borrow a CD: you have overdue CDs."),
                        "Should print message about overdue CDs");

            } finally {
                // Restore System.out
                System.setOut(originalOut);
            }

        } finally {
            // 9️⃣ Restore the original Loans.txt
            if (Files.exists(backupPath)) {
                Files.move(backupPath, loansPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(loansPath);
            }
        }
    }


    @Test
    void test_ReadCDMenuOption_eList() throws Exception {
        Object result = invokeReadCDMenuOption("1\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eList, result);
    }

    @Test
    void test_ReadCDMenuOption_eBorrow() throws Exception {
        Object result = invokeReadCDMenuOption("2\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eBorrow, result);
    }

    @Test
    void test_ReadCDMenuOption_eReturn() throws Exception {
        Object result = invokeReadCDMenuOption("3\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eReturn, result);
    }

    @Test
    void test_ReadCDMenuOption_eOverdue() throws Exception {
        Object result = invokeReadCDMenuOption("4\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eOverdue, result);
    }

    @Test
    void test_ReadCDMenuOption_eFind() throws Exception {
        Object result = invokeReadCDMenuOption("5\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eFind, result);
    }

    @Test
    void test_ReadCDMenuOption_eBack() throws Exception {
        Object result = invokeReadCDMenuOption("6\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eBack, result);
    }

    @Test
    void test_ReadCDMenuOption_invalidInput_thenBack() throws Exception {
        // invalid input 9, then 6 for eBack
        Object result = invokeReadCDMenuOption("9\n6\n");
        assertEquals(clsUserCDsMenuScreen.enCDMenuOptions.eBack, result);
    }




}
