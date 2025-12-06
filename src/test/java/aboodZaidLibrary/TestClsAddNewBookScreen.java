package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestClsAddNewBookScreen {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    private byte[] booksBackup; // Backup of Books.txt

    @BeforeEach
    void setUp() throws IOException {
        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Backup the original Books.txt content if it exists
        Path booksPath = Paths.get("Books.txt");
        if (Files.exists(booksPath)) {
            booksBackup = Files.readAllBytes(booksPath);
        } else {
            booksBackup = null;
        }

        // Set up user session with full permissions
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
        System.setIn(originalIn);

        clsUserSession.currentUser = null;

        // Restore the original Books.txt content
        Path booksPath = Paths.get("Books.txt");
        if (booksBackup != null) {
            Files.write(booksPath, booksBackup);
        } else if (Files.exists(booksPath)) {
            Files.delete(booksPath);
        }
    }

    // ===================== Tests for showAddNewBookScreen =====================
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
        Files.write(Paths.get("Books.txt"), "Existing#//#Author#//#12345\n".getBytes());

        String simulatedInput = "New Book\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("⚠️ A book with this ISBN already exists"));
    }

    @Test
    void testUserWithoutPermission() {
        clsUserSession.currentUser.setPermissions(0);

        String simulatedInput = "My Book\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        clsAddNewBookScreen.showAddNewBookScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ You do not have permission to add books"));
    }

    // ===================== Tests for private __ensureBooksFile =====================
    @Test
    void testEnsureBooksFile_FileDoesNotExist() throws Exception {
        Path filePath = Paths.get("Books.txt");
        if (Files.exists(filePath)) Files.delete(filePath);

        Method method = clsAddNewBookScreen.class.getDeclaredMethod("__ensureBooksFile");
        method.setAccessible(true);
        method.invoke(null);

        assertTrue(Files.exists(filePath), "Books.txt should be created if it does not exist");
    }

    @Test
    void testEnsureBooksFile_FileExistsButEmpty() throws Exception {
        Path filePath = Paths.get("Books.txt");
        Files.write(filePath, new byte[0]);

        Method method = clsAddNewBookScreen.class.getDeclaredMethod("__ensureBooksFile");
        method.setAccessible(true);
        method.invoke(null);

        assertTrue(Files.exists(filePath), "Books.txt should still exist after ensuring");
        assertEquals(0, Files.size(filePath), "Books.txt should remain empty");
    }

    @Test
    void testIsbnExists_EmptyFile() throws Exception {
        // Backup original file
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;
        if (Files.exists(filePath)) backup = Files.readAllBytes(filePath);

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Call private static method via reflection
            Method method = clsAddNewBookScreen.class.getDeclaredMethod("__isbnExists", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "12345");

            // Assert ISBN is not found
            assertFalse(result, "Empty file → ISBN should not exist");

        } finally {
            // Restore original file
            if (backup != null) Files.write(filePath, backup);
            else Files.deleteIfExists(filePath);
        }
    }

    @Test
    void testIsbnExists_FileDoesNotExist() throws Exception {
        // Backup original file
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;
        if (Files.exists(filePath)) {
            backup = Files.readAllBytes(filePath);
            Files.delete(filePath);
        }

        try {
            // Call private static method via reflection
            Method method = clsAddNewBookScreen.class.getDeclaredMethod("__isbnExists", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "12345");

            // Assert ISBN is not found because file doesn't exist
            assertFalse(result, "Missing file → ISBN should not exist");

        } finally {
            // Restore original file
            if (backup != null) Files.write(filePath, backup);
        }
    }


    @Test
    void testIsbnExists_BookExists() throws Exception {
        // Backup original file
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;
        if (Files.exists(filePath)) backup = Files.readAllBytes(filePath);

        try {
            // Empty the file and write a book line
            Files.write(filePath, "wq#//#wq#//#11\n".getBytes());

            // Call private static method via reflection
            Method method = clsAddNewBookScreen.class.getDeclaredMethod("__isbnExists", String.class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(null, "11");

            // Assert ISBN is found
            assertTrue(result, "ISBN '11' exists in file → should return true");

        } finally {
            // Restore original file
            if (backup != null) Files.write(filePath, backup);
            else Files.deleteIfExists(filePath);
        }
    }


    @Test
    void testAppendBook_Success() throws Exception {
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;

        // Backup the original file
        if (Files.exists(filePath)) {
            backup = Files.readAllBytes(filePath);
        }

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Call private static method via reflection
            Method method = clsAddNewBookScreen.class.getDeclaredMethod(
                    "__appendBook", String.class, String.class, String.class
            );
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "title", "author", "12");

            // Assert the method returned true
            assertTrue(result, "Appending book should return true");

            // Check that the file contains the expected line
            String content = Files.readAllLines(filePath).get(0);
            assertEquals("title#//#author#//#12", content, "Book line should be correctly written to file");

        } finally {
            // Restore the original file
            if (backup != null) Files.write(filePath, backup);
            else Files.deleteIfExists(filePath);
        }
    }

    @Test
    void testShowAddNewBookScreen_UserWithoutPermission() throws IOException {
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;

        // Backup the original file
        if (Files.exists(filePath)) {
            backup = Files.readAllBytes(filePath);
        }

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Set current user to a "found but empty" user
            clsUserSession.currentUser = clsUser.find("", "");

            // Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Call the method
            clsAddNewBookScreen.showAddNewBookScreen();

            // Restore System.out
            System.setOut(originalOut);

            String output = outContent.toString();

            // Check that the permission warning is printed
            assertTrue(output.contains("❌ You do not have permission to add books."),
                    "The method should block a user without permission");

        } finally {
            // Restore the original file
            if (backup != null) Files.write(filePath, backup);
            else Files.deleteIfExists(filePath);

            clsUserSession.currentUser = null;
        }
    }

    @Test
    void testShowAddNewBookScreen_EmptyTitle() throws IOException {
        // Ensure current user has full permissions
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

        // Simulate user input: empty title, author and ISBN (author and ISBN won't matter here)
        String simulatedInput = "\nJohn Doe\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call the method
        clsAddNewBookScreen.showAddNewBookScreen();

        // Restore System.out
        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("❌ Title cannot be empty"),
                "The method should warn when the title is empty");
    }

    @Test
    void testShowAddNewBookScreen_EmptyAuthor() throws IOException {
        // Ensure current user has full permissions
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

        // Simulate user input: title provided, empty author, ISBN (ISBN won't matter here)
        String simulatedInput = "My Book\n\n12345\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call the method
        clsAddNewBookScreen.showAddNewBookScreen();

        // Restore System.out
        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("❌ Author cannot be empty"),
                "The method should warn when the author is empty");
    }

    @Test
    void testShowAddNewBookScreen_EmptyISBN() throws IOException {
        // Ensure current user has full permissions
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

        // Simulate user input: title and author provided, empty ISBN
        String simulatedInput = "My Book\nJohn Doe\n\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Call the method
        clsAddNewBookScreen.showAddNewBookScreen();

        // Restore System.out
        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("❌ ISBN cannot be empty"),
                "The method should warn when the ISBN is empty");
    }

    @Test
    void testShowAddNewBookScreen_BookAlreadyExists() throws IOException {
        Path booksPath = Paths.get("Books.txt");
        Path backupPath = Paths.get("Books_backup.txt");

        // Backup the original file
        if (Files.exists(booksPath)) {
            Files.copy(booksPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file and add a book with ISBN 11
            Files.write(booksPath, "wq#//#wq#//#11\n".getBytes());

            // Ensure current user has full permissions
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

            // Simulate user input: title, author, ISBN that already exists
            String simulatedInput = "title\nauthor\n11\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            // Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Call the method
            clsAddNewBookScreen.showAddNewBookScreen();

            // Restore System.out
            System.setOut(originalOut);

            String output = outContent.toString();
            assertTrue(output.contains("⚠️ A book with this ISBN already exists"),
                    "The method should warn when the ISBN already exists");

            // Ensure the original line is still present, no duplicates
            String fileContent = new String(Files.readAllBytes(booksPath));
            assertEquals("wq#//#wq#//#11\n", fileContent, "The original book should remain, no duplicate added");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }
    @Test
    void testShowAddNewBookScreen_AddBookSuccessfully() throws IOException {
        Path booksPath = Paths.get("Books.txt");
        Path backupPath = Paths.get("Books_backup.txt");

        // Backup the original file
        if (Files.exists(booksPath)) {
            Files.copy(booksPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file
            Files.write(booksPath, new byte[0]);

            // Ensure current user has full permissions
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

            // Simulate user input: title, author, ISBN
            String simulatedInput = "title\nauthor\n1234\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            // Capture System.out
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Call the method
            clsAddNewBookScreen.showAddNewBookScreen();

            // Restore System.out
            System.setOut(originalOut);

            String output = outContent.toString();
            assertTrue(output.contains("✅ Book added successfully"),
                    "The success message should be printed when the book is added");

            // Verify the book was written to the file correctly
            String fileContent = new String(Files.readAllBytes(booksPath));
            assertTrue(fileContent.contains("title#//#author#//#1234"),
                    "The book should be correctly written to the file");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, booksPath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }

    @Test
    void testIsbnExists_NullLineOrCell() throws Exception {
        Path filePath = Paths.get("Books.txt");
        byte[] backup = null;
        if (Files.exists(filePath)) backup = Files.readAllBytes(filePath);

        try {
            // Write lines with null/empty cells (simulate null by writing actual "null" string)
            Files.write(filePath, ("null#//#Author#//#123\nTitle#//#null#//#456\n").getBytes());

            Method method = clsAddNewBookScreen.class.getDeclaredMethod("__isbnExists", String.class);
            method.setAccessible(true);

            // ISBN that matches a "null" cell (trimmed)
            boolean result1 = (boolean) method.invoke(null, "123");
            boolean result2 = (boolean) method.invoke(null, "456");

            assertTrue(result1, "Should find ISBN '123' even if line starts with 'null'");
            assertTrue(result2, "Should find ISBN '456' even if author cell is 'null'");

        } finally {
            if (backup != null) Files.write(filePath, backup);
            else Files.deleteIfExists(filePath);
        }
    }
    @Test
    void testIsbnExists_NullLineAndNullCellSimulation() throws Exception {
        Path booksPath = Paths.get("Books.txt");

        // Simulate "null line" with an empty line
        // Simulate "null cell" with consecutive separators (empty field)
        List<String> lines = Arrays.asList(
                "",                     // empty line → triggers line == null/empty branch
                "Title#//# #//#1234"    // second cell is empty → triggers cell null/empty check
        );
        Files.write(booksPath, lines);

        // Call the private __isbnExists method via reflection
        Method method = clsAddNewBookScreen.class.getDeclaredMethod("__isbnExists", String.class);
        method.setAccessible(true);

        boolean found1 = (boolean) method.invoke(null, "1234"); // should find the book
        boolean found2 = (boolean) method.invoke(null, "5678"); // should not find

        assertTrue(found1, "ISBN '1234' should be found even with empty cells");
        assertFalse(found2, "Non-existent ISBN should not be found");
    }


}
