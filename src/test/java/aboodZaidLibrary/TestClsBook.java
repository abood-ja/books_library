package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.Vector;

import org.junit.jupiter.api.*;

class TestClsBook {

    private static final String BOOKS_FILE = "Books.txt";
    private static final String BACKUP_FILE = "Books_backup.txt";

    @BeforeAll
    static void backupOriginalFile() throws IOException {
        Path originalPath = Paths.get(BOOKS_FILE);
        Path backupPath = Paths.get(BACKUP_FILE);

        if (Files.exists(originalPath)) {
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // If no original file, create empty backup to keep consistent
            Files.createFile(backupPath);
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean file for each test
        Files.deleteIfExists(Paths.get(BOOKS_FILE));
        Files.createFile(Paths.get(BOOKS_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(BOOKS_FILE));
    }

    @AfterAll
    static void restoreOriginalFile() throws IOException {
        Path originalPath = Paths.get(BOOKS_FILE);
        Path backupPath = Paths.get(BACKUP_FILE);

        // Delete test file if it exists
        Files.deleteIfExists(originalPath);

        // Restore the backup
        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath); // Optional: clean up backup after restore
        }
    }

    // --- Your tests below remain unchanged ---
    @Test
    void testCreateBookAndSettersGetters() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN001");
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("ISBN001", book.getISBN());
        assertFalse(book.isMarkedForDeleted());
        assertFalse(book.isEmpty());
    }

    @Test
    void testSave_AddNewBook_Success() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN002");
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        clsBook.enSaveResults result = book.save();
        assertEquals(clsBook.enSaveResults.svSucceeded, result);

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

        assertEquals(2, byTitle.size());
        assertEquals(1, byAuthor.size());
        assertEquals(1, byISBN.size());
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


    // Add these test methods to TestClsBook.java


    @Test
    void testIsBookExist() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN011");
        book.setTitle("Existence Test");
        book.setAuthor("Author Z");
        book.save();

        assertTrue(clsBook.isBookExist("ISBN011"));
        assertFalse(clsBook.isBookExist("ISBN999"));
    }

    @Test
    void testSave_EmptyMode() {
        // Create a book in empty mode (shouldn't be possible normally, but test the guard)
        clsBook emptyBook = new clsBook(clsBook.enMode.EmptyMode, "", "", "");

        clsBook.enSaveResults result = emptyBook.save();
        assertEquals(clsBook.enSaveResults.svFaildEmptyObject, result);
    }


    @Test
    void testMultipleBooksWithSameTitle() {
        clsBook book1 = clsBook.GetAddNewBookObject("ISBN013");
        book1.setTitle("Same Title");
        book1.setAuthor("Author A");
        book1.save();

        clsBook book2 = clsBook.GetAddNewBookObject("ISBN014");
        book2.setTitle("Same Title");
        book2.setAuthor("Author B");
        book2.save();

        Vector<clsBook> books = clsBook.findBooksByTitle("Same Title");
        assertEquals(2, books.size());

        // Verify both books are in the results
        boolean foundISBN013 = false;
        boolean foundISBN014 = false;

        for (clsBook book : books) {
            if (book.getISBN().equals("ISBN013")) foundISBN013 = true;
            if (book.getISBN().equals("ISBN014")) foundISBN014 = true;
        }

        assertTrue(foundISBN013);
        assertTrue(foundISBN014);
    }

    @Test
    void testMultipleBooksWithSameAuthor() {
        clsBook book1 = clsBook.GetAddNewBookObject("ISBN015");
        book1.setTitle("Book One");
        book1.setAuthor("Same Author");
        book1.save();

        clsBook book2 = clsBook.GetAddNewBookObject("ISBN016");
        book2.setTitle("Book Two");
        book2.setAuthor("Same Author");
        book2.save();

        Vector<clsBook> books = clsBook.findBooksByAuthor("Same Author");
        assertEquals(2, books.size());
    }

    @Test
    void testFindNonExistentBook() {
        clsBook book = clsBook.findBookByISBN("NONEXISTENT");
        assertTrue(book.isEmpty());

        clsBook bookByTitle = clsBook.findBookByTitle("Nonexistent Title");
        assertTrue(bookByTitle.isEmpty());

        clsBook bookByAuthor = clsBook.findBookByAuthor("Nonexistent Author");
        assertTrue(bookByAuthor.isEmpty());
    }

    @Test
    void testEmptyBooksList() {
        Vector<clsBook> books = clsBook.getBooksList();
        assertEquals(0, books.size());
    }

    @Test
    void testSettersModifyCorrectFields() {
        clsBook book = clsBook.GetAddNewBookObject("ISBN017");

        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());
        assertEquals("", book.getAuthor());
        assertEquals("ISBN017", book.getISBN());

        book.setAuthor("New Author");
        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals("ISBN017", book.getISBN());

        book.setISBN("ISBN018");
        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals("ISBN018", book.getISBN());
    }
}
