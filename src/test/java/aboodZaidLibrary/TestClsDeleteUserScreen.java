package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class TestClsDeleteUserScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        if (clsUserSession.currentUser == null){
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");}
        clsInputValidate.setTestScanner(null);
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);

        // Reset static state
        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null);
    }

    @Test
    void test_PrintUser() throws Exception {
        // Create a sample user
        clsUser user = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "testuser",
                "password",
                7 // example permissions
        );

        // Use reflection to access private static _PrintUser method
        Method printUserMethod = clsDeleteUserScreen.class.getDeclaredMethod("_PrintUser", clsUser.class);
        printUserMethod.setAccessible(true);

        // Invoke the method
        printUserMethod.invoke(null, user);

        // Capture the output
        String output = outputStream.toString();

        // Assertions to verify printed details
        assertTrue(output.contains("User Details:"));
        assertTrue(output.contains("User FirstName : Test"));
        assertTrue(output.contains("User LastName : User"));
        assertTrue(output.contains("User FullName : Test User"));
        assertTrue(output.contains("User Email : test@example.com"));
        assertTrue(output.contains("User UserName : testuser"));
        assertTrue(output.contains("User Permissions : 7"));
    }

    @Test
    void test_showDeleteUserScreen_CannotDeleteUser() throws Exception {
        // 1. Backup Users.txt and Loans.txt
        File usersFile = new File("Users.txt");
        File loansFile = new File("Loans.txt");
        File usersBackup = new File("Users_backup.txt");
        File loansBackup = new File("Loans_backup.txt");

        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), usersBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (loansFile.exists()) {
            Files.copy(loansFile.toPath(), loansBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Write test data
            try (FileWriter fw = new FileWriter(usersFile)) {
                fw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1\n");
            }
            try (FileWriter fw = new FileWriter(loansFile)) {
                fw.write("CD#//#10#//#user2#//#2025-11-28#//#2024-10-05#//#false\n");
            }

            // 3. Simulate user input: entering username "User2"
            String simulatedInput = "User2\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 4. Call the method
            clsDeleteUserScreen.showDeleteUserScreen();

            // 5. Capture output
            String output = outputStream.toString();
            assertTrue(output.contains("User Details:"));
            assertTrue(output.contains("User FirstName : zaid"));
            assertTrue(output.contains("User LastName : marabeh"));
            assertTrue(output.contains("❌ Cannot delete user"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // 6. Restore original files
            if (usersBackup.exists()) {
                Files.copy(usersBackup.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usersBackup.delete();
            }
            if (loansBackup.exists()) {
                Files.copy(loansBackup.toPath(), loansFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loansBackup.delete();
            }
        }
    }


    @Test
    void test_showDeleteUserScreen_NonExistentThenCannotDeleteUser() throws Exception {
        // 1. Backup Users.txt and Loans.txt
        File usersFile = new File("Users.txt");
        File loansFile = new File("Loans.txt");
        File usersBackup = new File("Users_backup.txt");
        File loansBackup = new File("Loans_backup.txt");

        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), usersBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (loansFile.exists()) {
            Files.copy(loansFile.toPath(), loansBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Write test data
            try (FileWriter fw = new FileWriter(usersFile)) {
                fw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1\n");
            }
            try (FileWriter fw = new FileWriter(loansFile)) {
                fw.write("CD#//#10#//#user2#//#2025-11-28#//#2024-10-05#//#false\n");
            }

            // 3. Simulate user input: first a non-existent username, then the valid username "User2"
            String simulatedInput = "NonExistentUser\nUser2\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 4. Call the method
            clsDeleteUserScreen.showDeleteUserScreen();

            // 5. Capture output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("❌ Username not found! Enter another:"));
            assertTrue(output.contains("User Details:"));
            assertTrue(output.contains("User FirstName : zaid"));
            assertTrue(output.contains("User LastName : marabeh"));
            assertTrue(output.contains("❌ Cannot delete user"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // 6. Restore original files
            if (usersBackup.exists()) {
                Files.copy(usersBackup.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usersBackup.delete();
            }
            if (loansBackup.exists()) {
                Files.copy(loansBackup.toPath(), loansFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loansBackup.delete();
            }
        }
    }


    @Test
    void test_showDeleteUserScreen_CanDeleteUser_Confirmyes() throws Exception {
        // 1. Backup Users.txt and Loans.txt
        File usersFile = new File("Users.txt");
        File loansFile = new File("Loans.txt");
        File usersBackup = new File("Users_backup.txt");
        File loansBackup = new File("Loans_backup.txt");

        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), usersBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (loansFile.exists()) {
            Files.copy(loansFile.toPath(), loansBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty both files
            PrintWriter writerUsers = new PrintWriter(usersFile);
            writerUsers.close();
            PrintWriter writerLoans = new PrintWriter(loansFile);
            writerLoans.close();

            // 3. Write test user data
            try (FileWriter fw = new FileWriter(usersFile)) {
                fw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1\n");
            }
            // Leave Loans.txt empty

            // 4. Simulate user input: enter username "User2" then confirm 'y'
            String simulatedInput = "User2\ny\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 5. Call the method
            clsDeleteUserScreen.showDeleteUserScreen();

            // 6. Capture output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("User Details:"));
            assertTrue(output.contains("User FirstName : zaid"));
            assertTrue(output.contains("User LastName : marabeh"));
            assertTrue(output.contains("✔ User Deleted Successfully!"));

            // 7. Verify the user is deleted from Users.txt
            String fileContent = String.join("\n", Files.readAllLines(usersFile.toPath()));
            assertFalse(fileContent.contains("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // 8. Restore original files
            if (usersBackup.exists()) {
                Files.copy(usersBackup.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usersBackup.delete();
            }
            if (loansBackup.exists()) {
                Files.copy(loansBackup.toPath(), loansFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loansBackup.delete();
            }
        }
    }

    @Test
    void test_showDeleteUserScreen_CanDeleteUser_ConfirmYes() throws Exception {
        // 1. Backup Users.txt and Loans.txt
        File usersFile = new File("Users.txt");
        File loansFile = new File("Loans.txt");
        File usersBackup = new File("Users_backup.txt");
        File loansBackup = new File("Loans_backup.txt");

        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), usersBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (loansFile.exists()) {
            Files.copy(loansFile.toPath(), loansBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty both files
            PrintWriter writerUsers = new PrintWriter(usersFile);
            writerUsers.close();
            PrintWriter writerLoans = new PrintWriter(loansFile);
            writerLoans.close();

            // 3. Write test user data
            try (FileWriter fw = new FileWriter(usersFile)) {
                fw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1\n");
            }
            // Leave Loans.txt empty

            // 4. Simulate user input: enter username "User2" then confirm 'y'
            String simulatedInput = "User2\nY\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 5. Call the method
            clsDeleteUserScreen.showDeleteUserScreen();

            // 6. Capture output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("User Details:"));
            assertTrue(output.contains("User FirstName : zaid"));
            assertTrue(output.contains("User LastName : marabeh"));
            assertTrue(output.contains("✔ User Deleted Successfully!"));

            // 7. Verify the user is deleted from Users.txt
            String fileContent = String.join("\n", Files.readAllLines(usersFile.toPath()));
            assertFalse(fileContent.contains("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // 8. Restore original files
            if (usersBackup.exists()) {
                Files.copy(usersBackup.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usersBackup.delete();
            }
            if (loansBackup.exists()) {
                Files.copy(loansBackup.toPath(), loansFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loansBackup.delete();
            }
        }
    }



    @Test
    void test_showDeleteUserScreen_CanDeleteUser_ConfirmNo() throws Exception {
        // 1. Backup Users.txt and Loans.txt
        File usersFile = new File("Users.txt");
        File loansFile = new File("Loans.txt");
        File usersBackup = new File("Users_backup.txt");
        File loansBackup = new File("Loans_backup.txt");

        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), usersBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        if (loansFile.exists()) {
            Files.copy(loansFile.toPath(), loansBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty both files
            PrintWriter writerUsers = new PrintWriter(usersFile);
            writerUsers.close();
            PrintWriter writerLoans = new PrintWriter(loansFile);
            writerLoans.close();

            // 3. Write test user data
            try (FileWriter fw = new FileWriter(usersFile)) {
                fw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1\n");
            }
            // Leave Loans.txt empty

            // 4. Simulate user input: enter username "User2" then cancel with 'n'
            String simulatedInput = "User2\nn\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 5. Call the method
            clsDeleteUserScreen.showDeleteUserScreen();

            // 6. Capture output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("User Details:"));
            assertTrue(output.contains("User FirstName : zaid"));
            assertTrue(output.contains("User LastName : marabeh"));
            // Confirm that deletion message does NOT appear
            assertFalse(output.contains("✔ User Deleted Successfully!"));

            // 7. Verify the user was NOT deleted
            String fileContent = String.join("\n", Files.readAllLines(usersFile.toPath()));
            assertTrue(fileContent.contains("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // 8. Restore original files
            if (usersBackup.exists()) {
                Files.copy(usersBackup.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usersBackup.delete();
            }
            if (loansBackup.exists()) {
                Files.copy(loansBackup.toPath(), loansFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                loansBackup.delete();
            }
        }
    }

}
