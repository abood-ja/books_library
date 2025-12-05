package aboodZaidLibrary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClsAddNewUserScreen {
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
}
