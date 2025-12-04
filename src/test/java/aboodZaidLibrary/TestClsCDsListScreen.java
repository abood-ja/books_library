package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsCDsListScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    private byte[] cdsBackup; // Backup of CDs.txt

    @BeforeEach
    void setUp() throws IOException {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        if (clsUserSession.currentUser == null) {
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");
        }
        // Backup CDs.txt if it exists
        Path cdsPath = Paths.get("CDs.txt");
        if (Files.exists(cdsPath)) {
            cdsBackup = Files.readAllBytes(cdsPath);
        } else {
            cdsBackup = null;
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        // Restore the original CDs.txt
        Path cdsPath = Paths.get("CDs.txt");
        if (cdsBackup != null) {
            Files.write(cdsPath, cdsBackup);
        } else if (Files.exists(cdsPath)) {
            Files.delete(cdsPath);
        }
    }

    @Test
    void testShowCDsListScreen_NoCDs() throws IOException {
        // Ensure file is empty for this test
        Path cdsPath = Paths.get("CDs.txt");
        Files.write(cdsPath, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        clsCDsListScreen.showCDsListScreen();

        String output = outputStream.toString();
        assertTrue(output.contains("No CDs Available In the System!"),
                "Output should indicate no CDs available");
    }

    @Test
    void testShowCDsListScreen_WithCDs() throws IOException {
        // Prepare some test CDs in the file
        String cd1 = "Abbey Road#//#The Beatles#//#CD001\n";
        String cd2 = "Thriller#//#Michael Jackson#//#CD002\n";

        Path cdsPath = Paths.get("CDs.txt");
        Files.write(cdsPath, (cd1 + cd2).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        clsCDsListScreen.showCDsListScreen();

        String output = outputStream.toString();

        // Check header
        assertTrue(output.contains("Title"));
        assertTrue(output.contains("Artist"));
        assertTrue(output.contains("CD ID"));

        // Check CD data
        assertTrue(output.contains("Abbey Road"));
        assertTrue(output.contains("The Beatles"));
        assertTrue(output.contains("CD001"));

        assertTrue(output.contains("Thriller"));
        assertTrue(output.contains("Michael Jackson"));
        assertTrue(output.contains("CD002"));
    }
}
