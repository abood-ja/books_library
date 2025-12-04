package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class TestClsUserListScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    private static final Path USERS_FILE = Paths.get("Users.txt");
    private static final Path USERS_FILE_BACKUP = Paths.get("Users.txt.bak");

    @BeforeAll
    static void backupOriginalFile() throws IOException {
        // Create a backup copy of the original file before any tests run
        if (Files.exists(USERS_FILE)) {
            Files.copy(USERS_FILE, USERS_FILE_BACKUP, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @AfterAll
    static void restoreOriginalFile() throws IOException {
        // Restore the original file after all tests complete
        if (Files.exists(USERS_FILE_BACKUP)) {
            Files.copy(USERS_FILE_BACKUP, USERS_FILE, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(USERS_FILE_BACKUP); // Clean up backup file
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // For each test, start with an empty Users.txt
        // But don't delete the backup - it's preserved separately
        Files.write(USERS_FILE, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Clear users in memory for testing
        clsUser.clearAllUsers();
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        // Just clear the file after each test - the backup will restore it later
        Files.write(USERS_FILE, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Reset users in memory
        clsUser.clearAllUsers();

        // Reset test scanner if used
        clsInputValidate.setTestScanner(null);
    }

    private void writeUserToFile(clsUser user) throws IOException {
        String line = user.getFirstName() + "#//#" +
                user.getLastName() + "#//#" +
                user.getEmail() + "#//#" +
                user.getPhone() + "#//#" +
                user.getUserName() + "#//#" +
                clsEncryption.encryptText(user.getPassword(), 2) + "#//#" +
                user.getPermissions() + "\n";
        Files.write(USERS_FILE, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Test
    void testShowUserListScreen_NoUsers() {
        clsUserListScreen.showUserListScreen();

        String output = outputStream.toString();
        assertTrue(output.contains("No Users Available In the System!"));
    }

    @Test
    void testShowUserListScreen_WithUsers() throws IOException {
        clsUser user1 = new clsUser(clsUser.enMode.AddNewMode, "John", "Doe", "john@example.com", "1234567890",
                "john123", "pass123", -1);
        clsUser user2 = new clsUser(clsUser.enMode.AddNewMode, "Jane", "Smith", "jane@example.com", "0987654321",
                "jane456", "pass456", 1);

        writeUserToFile(user1);
        writeUserToFile(user2);

        clsUserListScreen.showUserListScreen();

        String output = outputStream.toString();
        assertTrue(output.contains("User Name"));
        assertTrue(output.contains("Full Name"));
        assertTrue(output.contains("john123"));
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("pass123"));
        assertTrue(output.contains("jane456"));
        assertTrue(output.contains("Jane Smith"));
        assertTrue(output.contains("pass456"));
    }
}