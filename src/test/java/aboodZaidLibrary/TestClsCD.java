package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Vector;

import org.junit.jupiter.api.*;

class TestClsCD {

    private static final String CDS_FILE = "CDs.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean file before each test
        Files.deleteIfExists(Paths.get(CDS_FILE));
        Files.createFile(Paths.get(CDS_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(CDS_FILE));
    }

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
}
