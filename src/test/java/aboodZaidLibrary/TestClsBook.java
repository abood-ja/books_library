package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Vector;

import org.junit.jupiter.api.*;

class TestClsBook {

    private static final String BOOKS_FILE = "Books.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean file before each test
        Files.deleteIfExists(Paths.get(BOOKS_FILE));
        Files.createFile(Paths.get(BOOKS_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(BOOKS_FILE));
    }

    @Test
    void testCreateBookAndSettersGetters() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN001");
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("ISBN001", book.getISBN());
        assertFalse(book.isMarkedForDeleted());
        assertFalse(book.isEmpty()); // AddNewBookObject is not empty
    }

    @Test
    void testSave_AddNewBook_Success() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN002");
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        clsBook.enSaveResults result = book.save();
        assertEquals(clsBook.enSaveResults.svSucceeded, result);

        // Verify book is in file
        clsBook loaded = clsBook.findBookByISBN("ISBN002");
        assertEquals("Book Title", loaded.getTitle());
        assertEquals("Author Name", loaded.getAuthor());
    }

    @Test
    void testSave_AddNewBook_DuplicateISBN() {
        clsBook book1 = clsBook.GetAddNewBookObject("ISBN003");
        book1.setTitle("First Book");
        book1.setAuthor("Author1");
        book1.save();

        clsBook book2 = clsBook.GetAddNewBookObject("ISBN003");
        book2.setTitle("Second Book");
        book2.setAuthor("Author2");
        clsBook.enSaveResults result = book2.save();

        assertEquals(clsBook.enSaveResults.svFaildBookExists, result);
    }

    @Test
    void testDeleteBook() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN004");
        book.setTitle("To Delete");
        book.setAuthor("Author X");
        book.save();

        boolean deleted = book.delete();
        assertTrue(deleted);

        // After delete, findBookByISBN returns empty book
        clsBook loaded = clsBook.findBookByISBN("ISBN004");
        assertTrue(loaded.isEmpty());
    }

    @Test
    void testFindBookByTitleAuthorISBN() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN005");
        book.setTitle("FindMe");
        book.setAuthor("AuthorY");
        book.save();

        clsBook byTitle = clsBook.findBookByTitle("FindMe");
        clsBook byAuthor = clsBook.findBookByAuthor("AuthorY");
        clsBook byISBN = clsBook.findBookByISBN("ISBN005");

        assertEquals("FindMe", byTitle.getTitle());
        assertEquals("AuthorY", byAuthor.getAuthor());
        assertEquals("ISBN005", byISBN.getISBN());
    }

    @Test
    void testFindBooksByTitleAuthorISBN() {
        clsBook book1 = clsBook.GetAddNewBookObject("ISBN006");
        book1.setTitle("Title1");
        book1.setAuthor("Author1");
        book1.save();

        clsBook book2 = clsBook.GetAddNewBookObject("ISBN007");
        book2.setTitle("Title1");
        book2.setAuthor("Author2");
        book2.save();

        Vector<clsBook> byTitle = clsBook.findBooksByTitle("Title1");
        Vector<clsBook> byAuthor = clsBook.findBooksByAuthor("Author2");
        Vector<clsBook> byISBN = clsBook.findBooksByISBN("ISBN006");

        assertEquals(2, byTitle.size()); // Both books have Title1
        assertEquals(1, byAuthor.size()); // Only book2 has Author2
        assertEquals(1, byISBN.size()); // Only book1 has ISBN006
    }

    @Test
    void testGetBooksList() {
        clsBook book1 = clsBook.GetAddNewBookObject("ISBN008");
        book1.setTitle("Book1");
        book1.setAuthor("Author1");
        book1.save();

        clsBook book2 = clsBook.GetAddNewBookObject("ISBN009");
        book2.setTitle("Book2");
        book2.setAuthor("Author2");
        book2.save();

        Vector<clsBook> books = clsBook.getBooksList();
        assertEquals(2, books.size());
    }
}
