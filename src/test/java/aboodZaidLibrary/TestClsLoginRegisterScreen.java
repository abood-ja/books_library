package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TestClsLoginRegisterScreen {

    private static final Path LOGIN_REGISTER_FILE = Paths.get("LoginRegister.txt");
    private static byte[] backupData;

    @BeforeAll
    static void backupFile() throws IOException {
        // Backup the file contents
        if (Files.exists(LOGIN_REGISTER_FILE)) {
            backupData = Files.readAllBytes(LOGIN_REGISTER_FILE);
        }
    }

    @AfterAll
    static void restoreFile() throws IOException {
        // Restore the original file
        if (backupData != null) {
            Files.write(LOGIN_REGISTER_FILE, backupData);
        } else {
            Files.deleteIfExists(LOGIN_REGISTER_FILE);
        }
    }

    @BeforeEach
    void emptyFile() throws IOException {
        // Empty the file before each test
        Files.write(LOGIN_REGISTER_FILE, new byte[0]);
    }

    @Test
    void testShowLoginRegisterScreen_EmptyFile() {
        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the method
        clsLoginRegisterScreen.showLoginRegisterScreen();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output contains the "No Logins Available" message
        String output = outputStream.toString();
        assertTrue(output.contains("No Logins Available In the System!"));
    }

    @Test
    void testShowLoginRegisterScreen_WithRecord() throws IOException {
        // Step 2: Write a test record into the file
        String testRecord = "31/10/2025 - 16:08:42#//#User1#//#3456#//#-1";
        Files.write(LOGIN_REGISTER_FILE, testRecord.getBytes());

        // Capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the method
        clsLoginRegisterScreen.showLoginRegisterScreen();

        // Restore System.out
        System.setOut(originalOut);

        // Verify that the output contains the test record's username
        String output = outputStream.toString();
        assertTrue(output.contains("User1"), "Output should contain the username from the file");
        assertFalse(output.contains("No Logins Available In the System!"), "Output should not show 'No Logins Available'");
    }
}
