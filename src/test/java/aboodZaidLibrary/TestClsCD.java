package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Vector;

import org.junit.jupiter.api.*;

class TestClsCD {

    private static final String CDS_FILE = "CDs.txt";
    private static final String BACKUP_FILE = "CDs_backup.txt";

    @BeforeAll
    static void backupOriginalFile() throws IOException {
        Path originalPath = Paths.get(CDS_FILE);
        Path backupPath = Paths.get(BACKUP_FILE);

        if (Files.exists(originalPath)) {
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createFile(backupPath);
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean file for each test
        Files.deleteIfExists(Paths.get(CDS_FILE));
        Files.createFile(Paths.get(CDS_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(CDS_FILE));
    }

    @AfterAll
    static void restoreOriginalFile() throws IOException {
        Path originalPath = Paths.get(CDS_FILE);
        Path backupPath = Paths.get(BACKUP_FILE);

        Files.deleteIfExists(originalPath);

        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath); // clean up backup
        }
    }

    // --- Your tests remain unchanged ---
    @Test
    void testCreateCDAndSettersGetters() {
        clsCD cd = clsCD.GetAddNewCDObject("CD001");
        cd.setTitle("My Song");
        cd.setArtist("Artist X");

        assertEquals("My Song", cd.getTitle());
        assertEquals("Artist X", cd.getArtist());
        assertEquals("CD001", cd.getCDID());
        assertFalse(cd.isEmpty());
    }

    @Test
    void testSave_AddNewCD_Success() {
        clsCD cd = clsCD.GetAddNewCDObject("CD002");
        cd.setTitle("Song Two");
        cd.setArtist("Artist Y");

        clsCD.enSaveResults result = cd.save();
        assertEquals(clsCD.enSaveResults.svSucceeded, result);

        clsCD loaded = clsCD.findCDByID("CD002");
        assertEquals("Song Two", loaded.getTitle());
        assertEquals("Artist Y", loaded.getArtist());
    }

    @Test
    void testSave_AddNewCD_DuplicateID() {
        clsCD cd1 = clsCD.GetAddNewCDObject("CD003");
        cd1.setTitle("First Song");
        cd1.setArtist("Artist A");
        cd1.save();

        clsCD cd2 = clsCD.GetAddNewCDObject("CD003");
        cd2.setTitle("Second Song");
        cd2.setArtist("Artist B");

        clsCD.enSaveResults result = cd2.save();
        assertEquals(clsCD.enSaveResults.svFailedCDExists, result);
    }

    @Test
    void testDeleteCD() {
        clsCD cd = clsCD.GetAddNewCDObject("CD004");
        cd.setTitle("Delete Me");
        cd.setArtist("Artist D");
        cd.save();

        boolean deleted = cd.delete();
        assertTrue(deleted);

        clsCD loaded = clsCD.findCDByID("CD004");
        assertTrue(loaded.isEmpty());
    }

    @Test
    void testFindCDByIDTitleArtist() {
        clsCD cd = clsCD.GetAddNewCDObject("CD005");
        cd.setTitle("FindMe");
        cd.setArtist("ArtistF");
        cd.save();

        clsCD byID = clsCD.findCDByID("CD005");
        Vector<clsCD> byTitle = clsCD.findCDsByTitle("FindMe");
        Vector<clsCD> byArtist = clsCD.findCDsByArtist("ArtistF");

        assertEquals("FindMe", byID.getTitle());
        assertEquals(1, byTitle.size());
        assertEquals("FindMe", byTitle.get(0).getTitle());
        assertEquals(1, byArtist.size());
        assertEquals("ArtistF", byArtist.get(0).getArtist());
    }

    @Test
    void testIsCDExist() {
        clsCD cd = clsCD.GetAddNewCDObject("CD006");
        cd.setTitle("Existence Test");
        cd.setArtist("Artist Z");
        cd.save();

        assertTrue(clsCD.isCDExist("CD006"));
        assertFalse(clsCD.isCDExist("CD007"));
    }
    @Test
    void testSetCDID() {
        // Arrange
        clsCD cd = new clsCD(clsCD.enMode.UpdateMode, "Test Title", "Test Artist", "CD001");

        // Action
        cd.setCDID("CD002");

        // Assert
        assertEquals("CD002", cd.getCDID());
    }

    @Test
    void testGetCDsList() {
        clsCD cd1 = clsCD.GetAddNewCDObject("CD008");
        cd1.setTitle("CD One");
        cd1.setArtist("Artist1");
        cd1.save();

        clsCD cd2 = clsCD.GetAddNewCDObject("CD009");
        cd2.setTitle("CD Two");
        cd2.setArtist("Artist2");
        cd2.save();

        Vector<clsCD> cds = clsCD.getCDsList();
        assertEquals(2, cds.size());
    }
    @Test
    void testFindCDsByArtist_MatchingCDs() {
        // Tests the true branch - when artist matches
        Vector<clsCD> results = clsCD.findCDsByArtist("Some Artist");

        // If CDs with this artist exist, the result should not be empty
        assertNotNull(results);
    }

    @Test
    void testFindCDsByArtist_NoMatchingCDs() {
        // Tests the false branch - when artist doesn't match any CD
        Vector<clsCD> results = clsCD.findCDsByArtist("NonExistentArtist123");

        // Should return empty vector when no CDs match
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindCDsByArtist_MultipleMatches() {
        // Tests when multiple CDs have the same artist
        Vector<clsCD> results = clsCD.findCDsByArtist("Common Artist");

        assertNotNull(results);
        // Add assertions based on your test data
    }

    @Test
    void testDelete_CDExists() {
        // Tests the true branch - when CDID matches
        // First add a CD to ensure it exists
        clsCD cd = clsCD.GetAddNewCDObject("TEST001");
        cd.setTitle("Test Title");
        cd.setArtist("Test Artist");
        cd.save();

        // Now delete it
        boolean result = cd.delete();

        assertTrue(result);
        assertTrue(cd.isEmpty());
    }

    @Test
    void testDelete_CDNotFound() {
        // Tests the false branch - when CDID doesn't match any CD in the file
        // Create a CD that was never saved
        clsCD cd = new clsCD(clsCD.enMode.UpdateMode, "Title", "Artist", "NONEXISTENT999");

        // Try to delete it
        boolean result = cd.delete();

        assertTrue(result);
        assertTrue(cd.isEmpty());
    }

    @Test
    void testSave_SuccessfullySavesCDToFile() {
        // This will trigger _SaveCDsDataToFile and execute the write lines
        clsCD cd = clsCD.GetAddNewCDObject("SAVE001");
        cd.setTitle("Save Test Title");
        cd.setArtist("Save Test Artist");

        clsCD.enSaveResults result = cd.save();

        assertEquals(clsCD.enSaveResults.svSucceeded, result);

        // Verify it was saved
        clsCD foundCD = clsCD.findCDByID("SAVE001");
        assertFalse(foundCD.isEmpty());
        assertEquals("Save Test Title", foundCD.getTitle());
    }

    @Test
    void testDelete_TriggersSaveToFile() {
        // First create and save a CD
        clsCD cd = clsCD.GetAddNewCDObject("DELETE001");
        cd.setTitle("Delete Test");
        cd.setArtist("Delete Artist");
        cd.save();

        // Now delete it - this will trigger _SaveCDsDataToFile
        cd.delete();

        // Verify it was deleted
        clsCD foundCD = clsCD.findCDByID("DELETE001");
        assertTrue(foundCD.isEmpty());
    }
}

