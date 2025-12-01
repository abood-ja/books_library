package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsAddNewCDScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() throws IOException {
        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Clean CDs.txt before each test
        Path path = Paths.get("CDs.txt");
        if (Files.exists(path)) Files.delete(path);

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

        // Clean CDs.txt after test
        Path path = Paths.get("CDs.txt");
        if (Files.exists(path)) Files.delete(path);

        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null); // Reset test scanner
    }

    @Test
    void testAddNewCDSuccessfully() throws IOException {
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
