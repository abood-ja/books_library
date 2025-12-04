package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsBooksListScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    private byte[] booksBackup; // backup of Books.txt

    @BeforeEach
    void setUp() throws IOException {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Ensure currentUser is always initialized
        if (clsUserSession.currentUser == null) {
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");
        }

        Path booksPath = Paths.get("Books.txt");

        // Backup Books.txt if it exists
        if (Files.exists(booksPath)) {
            booksBackup = Files.readAllBytes(booksPath);
        } else {
            booksBackup = null;
        }

        // Ensure Books.txt exists and is empty for testing
        Files.write(booksPath, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        Path booksPath = Paths.get("Books.txt");

        // Restore original Books.txt after test
        if (booksBackup != null) {
            Files.write(booksPath, booksBackup, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } else if (Files.exists(booksPath)) {
            Files.delete(booksPath);
        }

        // Reset currentUser to prevent interference with other tests
        clsUserSession.currentUser = null;

        // Reset test scanner if used
        clsInputValidate.setTestScanner(null);
    }

    private void writeBookToFile(clsBook book) throws IOException {
        Path booksPath = Paths.get("Books.txt");
        String line = book.getTitle() + "#//#" + book.getAuthor() + "#//#" + book.getISBN() + "\n";
        Files.write(booksPath, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Test
    void testShowBooksListScreen_NoBooks() {
        clsBooksListScreen.showBooksListScreen();

        String output = outputStream.toString();
        assertTrue(output.contains("No Books Available In the System!"),
                "Output should indicate no books available");
    }

    @Test
    void testShowBooksListScreen_WithBooks() throws IOException {
        clsBook book1 = clsBook.GetAddNewBookObject("9780132350884");
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert Martin");

        clsBook book2 = clsBook.GetAddNewBookObject("9780134685991");
        book2.setTitle("Effective Java");
        book2.setAuthor("Joshua Bloch");

        writeBookToFile(book1);
        writeBookToFile(book2);

        clsBooksListScreen.showBooksListScreen();

        String output = outputStream.toString();

        assertTrue(output.contains("Title"));
        assertTrue(output.contains("Author"));
        assertTrue(output.contains("ISBN"));
        assertTrue(output.contains("Clean Code"));
        assertTrue(output.contains("Robert Martin"));
        assertTrue(output.contains("9780132350884"));
        assertTrue(output.contains("Effective Java"));
        assertTrue(output.contains("Joshua Bloch"));
        assertTrue(output.contains("9780134685991"));
    }
}
