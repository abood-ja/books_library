package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class clsCDTest {

    private static final String TEST_FILE = "CDs.txt";
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() throws IOException {
        // Delete and recreate test file before each test
        Path filePath = Paths.get(TEST_FILE);
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        // Capture System.out for screen tests
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    void testAddNewCDAndExistence() {
        clsCD cd = clsCD.GetAddNewCDObject("CD01");
        cd.setTitle("Test Title");
        cd.setArtist("Test Artist");

        assertEquals(clsCD.enSaveResults.svSucceeded, cd.save());
        assertTrue(clsCD.isCDExist("CD01"));
    }

    @Test
    void testDuplicateCDSaveFails() {
        clsCD cd1 = clsCD.GetAddNewCDObject("CD02");
        cd1.setTitle("Title1");
        cd1.setArtist("Artist1");
        cd1.save();

        clsCD cd2 = clsCD.GetAddNewCDObject("CD02");
        cd2.setTitle("Title2");
        cd2.setArtist("Artist2");

        assertEquals(clsCD.enSaveResults.svFailedCDExists, cd2.save());
    }

    @Test
    void testDeleteCD() {
        clsCD cd = clsCD.GetAddNewCDObject("CD03");
        cd.setTitle("Delete Me");
        cd.setArtist("Artist3");
        cd.save();

        assertTrue(clsCD.isCDExist("CD03"));
        assertTrue(cd.delete());
        assertFalse(clsCD.isCDExist("CD03"));
    }

    @Test
    void testFindCDByID() {
        clsCD cd = clsCD.GetAddNewCDObject("CD04");
        cd.setTitle("Find Me");
        cd.setArtist("Artist4");
        cd.save();

        clsCD found = clsCD.findCDByID("CD04");
        assertEquals("Find Me", found.getTitle());
        assertEquals("Artist4", found.getArtist());

        clsCD notFound = clsCD.findCDByID("NON_EXIST");
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testShowCDsListScreenOutput() {
        clsCD cd = clsCD.GetAddNewCDObject("CD05");
        cd.setTitle("Screen Test");
        cd.setArtist("Artist5");
        cd.save();

        clsCDsListScreen.showCDsListScreen();

        String output = outContent.toString();
        assertTrue(output.contains("Screen Test"));
        assertTrue(output.contains("Artist5"));
        assertTrue(output.contains("CD05"));
    }

    @Test
    void testFindCDsByTitleAndArtist() {
        clsCD cd1 = clsCD.GetAddNewCDObject("CD06");
        cd1.setTitle("TitleA");
        cd1.setArtist("ArtistX");
        cd1.save();

        clsCD cd2 = clsCD.GetAddNewCDObject("CD07");
        cd2.setTitle("TitleA");
        cd2.setArtist("ArtistY");
        cd2.save();

        Vector<clsCD> byTitle = clsCD.findCDsByTitle("TitleA");
        assertEquals(2, byTitle.size());

        Vector<clsCD> byArtist = clsCD.findCDsByArtist("ArtistX");
        assertEquals(1, byArtist.size());
        assertEquals("CD06", byArtist.get(0).getCDID());
    }
}
