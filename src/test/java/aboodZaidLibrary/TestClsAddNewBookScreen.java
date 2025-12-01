package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsAddNewBookScreen {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() throws IOException {
        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Clean Books.txt before each test
        Path path = Paths.get("Books.txt");
        if (Files.exists(path)) Files.delete(path);

        // Create a user with proper constructor and grant add book permission
        clsUserSession.currentUser = new clsUser(
                clsUser.enMode.AddNewMode,   // mode
                "John",                      // firstName
                "Doe",                       // lastName
                "john@example.com",           // email
                "12345678",                   // phone
                "johndoe",                   // username
                "password123",               // password
                clsUser.enPermissions.eAll.getValue() // full permission
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        System.setIn(originalIn);

        // Clean Books.txt after test
        Path path = Paths.get("Books.txt");
        if (Files.exists(path)) Files.delete(path);

        clsUserSession.currentUser = null;
    }

    @Test
    void testAddNewBookSuccessfully() throws IOException {
        String simulatedInput = "My Book\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("✅ Book added successfully"));

        String content = new String(Files.readAllBytes(Paths.get("Books.txt")));
        assertTrue(content.contains("My Book#//#John Doe#//#12345"));
    }

    @Test
    void testAddBookWithEmptyTitle() {
        String simulatedInput = "\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ Title cannot be empty"));
    }

    @Test
    void testAddBookWithEmptyAuthor() {
        String simulatedInput = "My Book\n\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ Author cannot be empty"));
    }

    @Test
    void testAddBookWithEmptyISBN() {
        String simulatedInput = "My Book\nJohn Doe\n\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ ISBN cannot be empty"));
    }

    @Test
    void testAddBookWithExistingISBN() throws IOException {
        // Pre-add a book with the same ISBN
        Files.write(Paths.get("Books.txt"), "Existing#//#Author#//#12345\n".getBytes());

        String simulatedInput = "New Book\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("⚠️ A book with this ISBN already exists"));
    }

    @Test
    void testUserWithoutPermission() {
        // Revoke all permissions
        clsUserSession.currentUser.setPermissions(0);

        String simulatedInput = "My Book\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ You do not have permission to add books"));
    }
}
