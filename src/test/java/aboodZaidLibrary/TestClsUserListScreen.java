package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestClsUserListScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Clear Users.txt before each test
        clsUser.clearAllUsers();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);

        // Clean up Users.txt after each test
        clsUser.clearAllUsers();
    }

    private void writeUserToFile(clsUser user) throws IOException {
        Path file = Paths.get("Users.txt");
        Files.write(file,
                (user.getFirstName() + "#//#" +
                        user.getLastName() + "#//#" +
                        user.getEmail() + "#//#" +
                        user.getPhone() + "#//#" +
                        user.getUserName() + "#//#" +
                        clsEncryption.encryptText(user.getPassword(), 2) + "#//#" +
                        user.getPermissions() + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Test
    void testShowUserListScreen_NoUsers() {
        clsUserListScreen.showUserListScreen();

        String output = outputStream.toString();
        assertTrue(output.contains("No Users Available In the System!"), "Output should indicate no users");
    }

    @Test
    void testShowUserListScreen_WithUsers() throws IOException {
        // Create sample users
        clsUser user1 = new clsUser(clsUser.enMode.AddNewMode, "John", "Doe", "john@example.com", "1234567890",
                "john123", "pass123", -1);
        clsUser user2 = new clsUser(clsUser.enMode.AddNewMode, "Jane", "Smith", "jane@example.com", "0987654321",
                "jane456", "pass456", 1);

        writeUserToFile(user1);
        writeUserToFile(user2);

        clsUserListScreen.showUserListScreen();

        String output = outputStream.toString();

        // Check table headers
        assertTrue(output.contains("User Name"));
        assertTrue(output.contains("Full Name"));

        // Check user data
        assertTrue(output.contains("john123"));
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("pass123"));
        assertTrue(output.contains("jane456"));
        assertTrue(output.contains("Jane Smith"));
        assertTrue(output.contains("pass456"));
    }
}
