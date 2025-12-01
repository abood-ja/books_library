package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsBooksListScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Ensure Books.txt exists (even if empty)
        Files.write(Paths.get("Books.txt"), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        // Clean up Books.txt after each test
        Files.deleteIfExists(Paths.get("Books.txt"));
    }

    private void writeBookToFile(clsBook book) throws IOException {
        Path file = Paths.get("Books.txt");
        String line = book.getTitle() + "#//#" + book.getAuthor() + "#//#" + book.getISBN() + "\n";
        Files.write(file, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
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
        // Create sample books using public factory
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

        // Check table header
        assertTrue(output.contains("Title"));
        assertTrue(output.contains("Author"));
        assertTrue(output.contains("ISBN"));

        // Check book data
        assertTrue(output.contains("Clean Code"));
        assertTrue(output.contains("Robert Martin"));
        assertTrue(output.contains("9780132350884"));
        assertTrue(output.contains("Effective Java"));
        assertTrue(output.contains("Joshua Bloch"));
        assertTrue(output.contains("9780134685991"));
    }
}
