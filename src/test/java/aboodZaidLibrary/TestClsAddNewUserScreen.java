package aboodZaidLibrary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClsAddNewUserScreen {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws Exception {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // 1. Backup original Users.txt
        File usersFile = new File("Users.txt");
        File backupFile = new File("Users_backup.txt");
        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // 2. Empty file and write test line BEFORE initializing currentUser
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile, false))) {
            bw.write("zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1");
            bw.newLine();
        }

        // 3. Now safely initialize currentUser
        clsUserSession.currentUser = clsUser.find("", "");

        // 4. Reset input scanner
        clsInputValidate.setTestScanner(null);

        clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
        clsUserSession.currentUser.setFirstName("Test");
        clsUserSession.currentUser.setLastName("User");
    }
    @AfterEach
    void tearDown() throws Exception {
        // Restore original System.out
        System.setOut(originalOut);

        // Reset static state
        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null);

        // Restore Users.txt
        File usersFile = new File("Users.txt");
        File backupFile = new File("Users_backup.txt");
        if (backupFile.exists()) {
            Files.copy(backupFile.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupFile.delete();
        }
    }


    @BeforeEach
    void backupUsersFile() throws Exception {
        File usersFile = new File("Users.txt");
        File backupFile = new File("Users_backup.txt");
        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Backup created: " + backupFile.getAbsolutePath());
        }
    }

    @AfterEach
    void restoreUsersFile() throws Exception {
        File usersFile = new File("Users.txt");
        File backupFile = new File("Users_backup.txt");
        if (backupFile.exists()) {
            Files.copy(backupFile.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupFile.delete();
            System.out.println("Users.txt restored from backup");
        }
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
    void test_ReadUserInfo() throws Exception {
        // Prepare simulated user input
        String simulatedInput = String.join(System.lineSeparator(),
                "John",        // FirstName
                "Doe",         // LastName
                "john@example.com", // Email
                "5551234567",  // Phone
                "mypassword",  // Password
                "y",           // Full access? yes -> permissions = -1
                "n",           // Add New Book? (ignored because full access)
                "n",           // Delete Book? (ignored)
                "n"            // Manage Users? (ignored)
        );

        // Set simulated input for clsInputValidate
        clsInputValidate.setTestScanner(new java.util.Scanner(simulatedInput));

        // Create user object
        clsUser user = new clsUser(
                clsUser.enMode.AddNewMode,
                "OldFirst",
                "OldLast",
                "old@example.com",
                "0000000000",
                "testuser",
                "oldpassword",
                0
        );

        // Access the private _ReadUserInfo method via reflection
        Method readUserInfoMethod = clsAddUserScreen.class.getDeclaredMethod("_ReadUserInfo", clsUser.class);
        readUserInfoMethod.setAccessible(true);

        // Invoke the method
        readUserInfoMethod.invoke(null, user);

        // Assertions to verify updated user info
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("5551234567", user.getPhone());
        assertEquals("mypassword", user.getPassword());
        assertEquals(-1, user.getPermissions()); // full access
    }


    @Test
    void test_FullAccessImmediately() throws Exception {
        // Simulate input: 'y' for full access
        String simulatedInput = "y";
        clsInputValidate.setTestScanner(new java.util.Scanner(simulatedInput));

        // Access private static method via reflection
        Method readPermissionsMethod = clsAddUserScreen.class
                .getDeclaredMethod("_ReadPermissionsToSet");
        readPermissionsMethod.setAccessible(true);

        // Invoke the method
        int permissions = (int) readPermissionsMethod.invoke(null);

        // Assert that it returned -1 for full access
        assertEquals(-1, permissions);

        // Optionally, check the console output contains the first question
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Do you want to give full access y/n?"));
    }


    @Test
    void test_AllIndividualPermissions() throws Exception {
        // Simulate input:
        // n -> full access
        // y -> Add New Book
        // y -> Delete Book
        // y -> Manage Users
        String simulatedInput = String.join(System.lineSeparator(),
                "n", // Full access? No
                "y", // Add New Book
                "y", // Delete Book
                "y"  // Manage Users
        );

        clsInputValidate.setTestScanner(new java.util.Scanner(simulatedInput));

        // Access private static method via reflection
        Method readPermissionsMethod = clsAddUserScreen.class
                .getDeclaredMethod("_ReadPermissionsToSet");
        readPermissionsMethod.setAccessible(true);

        // Invoke the method
        int permissions = (int) readPermissionsMethod.invoke(null);

        // Calculate expected permissions
        int expected = clsUser.enPermissions.pAddNewBook.getValue()
                + clsUser.enPermissions.pDeleteBook.getValue()
                + clsUser.enPermissions.pManageUsers.getValue();

        // Assert the permissions are correctly summed
        assertEquals(expected, permissions);

        // Optional: check the console output contains prompts
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Do you want to give full access y/n?"));
        Assertions.assertTrue(output.contains("Add New Book? y/n?"));
        Assertions.assertTrue(output.contains("Delete Book? y/n?"));
        Assertions.assertTrue(output.contains("Manage Users? y/n?"));
    }

    @Test
    void test_NoPermissionsSelected() throws Exception {
        // Simulate input:
        // n -> full access
        // n -> Add New Book
        // n -> Delete Book
        // n -> Manage Users
        String simulatedInput = String.join(System.lineSeparator(),
                "n", // Full access? No
                "n", // Add New Book
                "n", // Delete Book
                "n"  // Manage Users
        );

        clsInputValidate.setTestScanner(new java.util.Scanner(simulatedInput));

        // Access private static method via reflection
        Method readPermissionsMethod = clsAddUserScreen.class
                .getDeclaredMethod("_ReadPermissionsToSet");
        readPermissionsMethod.setAccessible(true);

        // Invoke the method
        int permissions = (int) readPermissionsMethod.invoke(null);

        // Assert permissions = 0
        assertEquals(0, permissions);

        // Optional: check the prompts were printed
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("Do you want to give full access y/n?"));
        Assertions.assertTrue(output.contains("Add New Book? y/n?"));
        Assertions.assertTrue(output.contains("Delete Book? y/n?"));
        Assertions.assertTrue(output.contains("Manage Users? y/n?"));
    }

    @Test
    void test_ShowAddUserScreen_WrongThenCorrectUsername() throws Exception {
        // 1. Backup Users.txt
        File usersFile = new File("Users.txt");
        File backupFile = new File("Users_backup.txt");
        if (usersFile.exists()) {
            Files.copy(usersFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty the file and add a single existing user
            try (PrintWriter writer = new PrintWriter(usersFile)) {
                writer.write("ahmad#//#mara#//#s12218431@stu.najah.edu#//#1234444#//#ahmad#//#3456#//#0\n");
            }

            // 3. Set the current user with full permissions
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "John",
                    "Doe",
                    "john@example.com",
                    "12345678",
                    "johndoe",
                    "password123",
                    clsUser.enPermissions.eAll.getValue()
            );

            // 4. Prepare simulated input
            String simulatedInput = String.join(System.lineSeparator(),
                    "ahmad",   // First attempt: username already exists
                    "User1",   // Second attempt: new valid username
                    "Ahmad",   // FirstName
                    "Mara",    // LastName
                    "ahmad@example.com", // Email
                    "5551234567", // Phone
                    "mypassword", // Password
                    "y",      // Full access
                    "n", "n", "n" // Add/Delete/Manage Users (ignored because full access)
            );

            clsInputValidate.setTestScanner(new Scanner(simulatedInput));

            // 5. Call the method
            clsAddUserScreen.showAddUserScreen();

            // 6. Capture the output
            String output = outputStream.toString();

            // 7. Assertions on output
            assertTrue(output.contains("This Username is already used, choose another one:"),
                    "Expected message about already used username when first entering 'ahmad'");
            assertTrue(output.contains("User Added successfully"),
                    "Expected success message after entering a valid new username");

            // 8. Verify the file now has 2 lines (original + new user)
            long linesCount = Files.lines(usersFile.toPath()).count();
            assertEquals(2, linesCount, "Expected Users.txt to contain 2 lines after adding the new user");

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore original Users.txt
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), usersFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                backupFile.delete();
            }
        }
    }


    @Test
    void test_ReadPermissions_Mixed() throws Exception {
        // Simulate mixed input
        String simulatedInput = String.join(System.lineSeparator(),
                "n", // Full access? No
                "y", // Add New Book
                "n", // Delete Book
                "y"  // Manage Users
        );

        clsInputValidate.setTestScanner(new Scanner(simulatedInput));

        Method readPermissionsMethod = clsAddUserScreen.class
                .getDeclaredMethod("_ReadPermissionsToSet");
        readPermissionsMethod.setAccessible(true);

        int permissions = (int) readPermissionsMethod.invoke(null);

        int expected = clsUser.enPermissions.pAddNewBook.getValue()
                + clsUser.enPermissions.pManageUsers.getValue(); // only Add and Manage
        assertEquals(expected, permissions);

        String output = outputStream.toString();
        assertTrue(output.contains("Do you want to give full access y/n?"));
        assertTrue(output.contains("Add New Book? y/n?"));
        assertTrue(output.contains("Delete Book? y/n?"));
        assertTrue(output.contains("Manage Users? y/n?"));
    }


    @Test
    void test_ReadPermissionsToSet_MixedPermissions() throws Exception {
        // Simulate input:
        // n -> full access
        // y -> Add New Book
        // n -> Delete Book
        // y -> Manage Users
        String simulatedInput = String.join(System.lineSeparator(),
                "n", // Full access? No
                "y", // Add New Book
                "n", // Delete Book
                "y"  // Manage Users
        );

        clsInputValidate.setTestScanner(new Scanner(simulatedInput));

        Method readPermissionsMethod = clsAddUserScreen.class
                .getDeclaredMethod("_ReadPermissionsToSet");
        readPermissionsMethod.setAccessible(true);

        int permissions = (int) readPermissionsMethod.invoke(null);

        int expected = clsUser.enPermissions.pAddNewBook.getValue()
                + clsUser.enPermissions.pManageUsers.getValue();

        assertEquals(expected, permissions);

        String output = outputStream.toString();
        assertTrue(output.contains("Do you want to give full access y/n?"));
        assertTrue(output.contains("Add New Book? y/n?"));
        assertTrue(output.contains("Delete Book? y/n?"));
        assertTrue(output.contains("Manage Users? y/n?"));
    }


}
