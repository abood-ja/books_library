package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsAddNewCDScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private Path backupPath = Paths.get("CDs_backup.txt");

    @BeforeEach
    void setUp() throws IOException {
        // Backup the original file
        Path originalPath = Paths.get("CDs.txt");
        if (Files.exists(originalPath)) {
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a user with full permission
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
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        // Restore the original file
        Path originalPath = Paths.get("CDs.txt");
        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath);
        }

        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null); // Reset test scanner
    }

    @Test
    void testAddNewCDSuccessfully() throws IOException {
        // Ensure file is empty for this test
        Files.write(Paths.get("CDs.txt"), new byte[0]);

        String simulatedInput = "Best Hits\nArtist Name\nCD123\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("✅ CD added successfully"));

        String content = new String(Files.readAllBytes(Paths.get("CDs.txt")));
        assertTrue(content.contains("Best Hits#//#Artist Name#//#CD123"));
    }

    @Test
    void testAddCDWithExistingID() throws IOException {
        // Pre-add a CD with the same ID
        Files.write(Paths.get("CDs.txt"), "Old Album#//#Old Artist#//#CD123\n".getBytes());

        String simulatedInput = "New Album\nNew Artist\nCD123\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("⚠️ A CD with this ID already exists!"));
    }

    @Test
    void testUserWithoutPermission() {
        // Remove permissions
        clsUserSession.currentUser.setPermissions(0);

        String simulatedInput = "Album\nArtist\nCD001\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ You do not have permission to add CDs."));
    }
}
