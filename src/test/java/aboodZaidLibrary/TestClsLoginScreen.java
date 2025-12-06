package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TestClsLoginScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;
    private clsUser testUser;

    @BeforeEach
    void setUp() {
        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create test user
        testUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "qbr",
                "password",
                0
        );
    }

    private static final String USERS_FILE = "Users.txt";
    private static String usersBackup;

    @BeforeAll
    static void backupUsersFile() throws Exception {
        // Backup Users.txt content
        usersBackup = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(USERS_FILE)));
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        clsInputValidate.setTestScanner(null);
        clsUserSession.currentUser = null;
    }
    @AfterAll
    static void restoreUsersFile() throws Exception {
        // Restore original Users.txt content
        java.nio.file.Files.write(java.nio.file.Paths.get(USERS_FILE), usersBackup.getBytes());
    }


    @Test
    void test_showLoginScreen_Exit() throws Exception {
        // Ensure currentUser is never null
        clsUserSession.currentUser = clsUser.find("", "");

        // Simulate user input "3" to exit immediately
        clsInputValidate.setTestScanner(new Scanner("3\n"));

        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Call the method
        boolean result = clsLoginScreen.showLoginScreen();

        // Capture output
        String output = outputStream.toString();

        // Assertions
        assertFalse(result, "Choosing 3 should return false (exit).");
        assertTrue(output.contains("Goodbye"), "Output should contain the goodbye message.");

        // Restore System.out and reset test Scanner
        System.setOut(originalOut);
        clsInputValidate.setTestScanner(null);
        clsUserSession.currentUser = null; // reset after test
    }

    @Test
    void test_RegisterNewUser_UserAlreadyExists() throws Exception {
        // Ensure currentUser is never null
        clsUserSession.currentUser = clsUser.find("", "");

        // Step 2: Empty Users.txt and add existing user
        String existingUser = "zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1";
        java.nio.file.Files.write(java.nio.file.Paths.get(USERS_FILE), existingUser.getBytes());

        // Step 3: Simulate input for the existing username using System.in
        String simulatedInput = "User2\n"; // Only username is needed
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Step 4: Access private static _RegisterNewUser() via reflection
        Method method = clsLoginScreen.class.getDeclaredMethod("_RegisterNewUser");
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(null);

        String output = outputStream.toString();

        // Assertions
        assertFalse(result, "Registering an existing user should fail.");
        assertTrue(output.contains("Username already exists"), "Output should indicate username exists.");

        // Restore System.in and System.out
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void test_SendWelcomeEmail_PrintsOutput() throws Exception {
        // Access private static _SendWelcomeEmail via reflection
        Method sendEmailMethod = clsLoginScreen.class.getDeclaredMethod("_SendWelcomeEmail", clsUser.class);
        sendEmailMethod.setAccessible(true);

        // Call the method with the test user
        sendEmailMethod.invoke(null, testUser);

        // Capture output
        String output = outputStream.toString();

        // Check that output contains some key lines
        assertTrue(output.contains("Preparing welcome message for:"), "Output should indicate preparing message");
        assertTrue(output.contains("To: " + testUser.getEmail()), "Output should show recipient email");
        assertTrue(output.contains("Hello " + testUser.getFirstName() + ","), "Output should greet the user");
    }


    @Test
    void test_Login_InvalidUsername_ThreeTimes() throws Exception {
        // Empty Users.txt
        java.nio.file.Files.write(java.nio.file.Paths.get("Users.txt"), "".getBytes());

        // Simulate input for three invalid usernames
        clsInputValidate.setTestScanner(new Scanner("abood\nabood\nabood\n"));

        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Call private static _Login()
        Method method = clsLoginScreen.class.getDeclaredMethod("_Login");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null);

        // Assertions
        String output = outputStream.toString();
        assertFalse(result, "Login should fail after 3 invalid username attempts");
        assertTrue(output.contains("Locked after 3 failed attempts"), "Output should contain lock message");

        // Restore
        System.setOut(originalOut);
        clsInputValidate.setTestScanner(null);
    }
    @Test
    void test_Login_WrongPassword_ThreeTimes() throws Exception {
        // Step 1 & 2: Empty Users.txt
        Files.write(Paths.get(USERS_FILE), "".getBytes());

        // Step 3: Add the test user
        String userLine = "zaid#//#marabeh#//#Hilal@Gmail.com#//#83983948#//#User2#//#3456#//#-1";
        Files.write(Paths.get(USERS_FILE), userLine.getBytes());

        // Step 4, 5 & 6: Simulate input for username and wrong password 3 times
        String simulatedInput = "User2\n7777\n7777\n7777\n";
        clsInputValidate.setTestScanner(new Scanner(simulatedInput));

        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Step 7: Call private static _Login()
        Method method = clsLoginScreen.class.getDeclaredMethod("_Login");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null);

        // Assertions
        String output = outputStream.toString();
        assertFalse(result, "Login should fail after 3 wrong password attempts.");
        assertTrue(output.contains("Locked after 3 failed attempts"), "Output should indicate account locked.");

        // Restore System.out and test Scanner
        System.setOut(originalOut);
        clsInputValidate.setTestScanner(null);
    }
}
