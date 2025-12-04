package aboodZaidLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TestClsFindCDScreen {

    @BeforeEach
    void setUpUser() {
        // Initialize currentUser if null to prevent NullPointerException
        if (clsUserSession.currentUser == null) {
            clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
            clsUserSession.currentUser.setFirstName("Test");
            clsUserSession.currentUser.setLastName("User");
        }
    }

    @Test
    void testPrintCD() throws Exception {
        // Create a dummy CD instance
        clsCD cd = new clsCD(clsCD.enMode.AddNewMode, "Thriller", "Michael Jackson", "CD123");

        // Access the private static _PrintCD method via reflection
        Method printCDMethod = clsFindCDScreen.class.getDeclaredMethod("_PrintCD", clsCD.class);
        printCDMethod.setAccessible(true);

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Invoke the private method
            printCDMethod.invoke(null, cd);

            // Get the printed output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("CD Details:"));
            assertTrue(output.contains("Title   : Thriller"));
            assertTrue(output.contains("Artist  : Michael Jackson"));
            assertTrue(output.contains("CD ID   : CD123"));

        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }


    @Test
    void testShowSearchResults_EmptyVector() throws Exception {
        // Create empty vector
        Vector<clsCD> cds = new Vector<>();

        // Access private method
        Method method = clsFindCDScreen.class.getDeclaredMethod("_ShowSearchResults", Vector.class);
        method.setAccessible(true);

        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Invoke method
            method.invoke(null, cds);

            // Get output
            String output = outputStream.toString();

            // Assert
            assertTrue(output.contains("No CDs found :-("));

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testShowSearchResults_WithCDs() throws Exception {
        // Create vector with one CD
        Vector<clsCD> cds = new Vector<>();
        clsCD cd = new clsCD(clsCD.enMode.AddNewMode, "Thriller", "Michael Jackson", "CD123");
        cds.add(cd);

        // Access private method
        Method method = clsFindCDScreen.class.getDeclaredMethod("_ShowSearchResults", Vector.class);
        method.setAccessible(true);

        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Invoke method
            method.invoke(null, cds);

            // Get output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("CD(s) Found:"));
            assertTrue(output.contains("Title   : Thriller"));
            assertTrue(output.contains("Artist  : Michael Jackson"));
            assertTrue(output.contains("CD ID   : CD123"));

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void TestSearchCDByTitle() throws Exception {

        Path cdsFile = Paths.get("CDs.txt");
        Path backupFile = Paths.get("CDs_backup.txt");

        // Backup CDs.txt if it exists
        if (Files.exists(cdsFile)) {
            Files.copy(cdsFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty CDs.txt and write test line
            Files.write(cdsFile, "ty#//#tys#//#10".getBytes());

            // Simulate input: choose 1 (title), then "ty"
            String fakeInput = "1\nty\n";
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture console output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method under test
            clsFindCDScreen.showFindCDScreen();

            // Get output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("CD(s) Found:"));
            assertTrue(output.contains("Title   : ty"));
            assertTrue(output.contains("Artist  : tys"));
            assertTrue(output.contains("CD ID   : 10"));

        } finally {
            // Restore CDs.txt from backup
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, cdsFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore system out
            System.setOut(System.out);
        }
    }


    @Test
    void TestSearchCDByArtist() throws Exception {

        Path cdsFile = Paths.get("CDs.txt");
        Path backupFile = Paths.get("CDs_backup.txt");

        // Backup CDs.txt if it exists
        if (Files.exists(cdsFile)) {
            Files.copy(cdsFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty CDs.txt and write test line
            Files.write(cdsFile, "ty#//#tys#//#10".getBytes());

            // Simulate input: choose 2 (artist), then "tys"
            String fakeInput = "2\ntys\n";
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture console output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method under test
            clsFindCDScreen.showFindCDScreen();

            // Get output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("CD(s) Found:"));
            assertTrue(output.contains("Title   : ty"));
            assertTrue(output.contains("Artist  : tys"));
            assertTrue(output.contains("CD ID   : 10"));

        } finally {
            // Restore CDs.txt from backup
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, cdsFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore system out
            System.setOut(System.out);
        }
    }


    @Test
    void TestSearchCDByID() throws Exception {

        Path cdsFile = Paths.get("CDs.txt");
        Path backupFile = Paths.get("CDs_backup.txt");

        // Backup CDs.txt if it exists
        if (Files.exists(cdsFile)) {
            Files.copy(cdsFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty CDs.txt and write test line
            Files.write(cdsFile, "ty#//#tys#//#10".getBytes());

            // Simulate input: choose 3 (CD ID), then "10"
            String fakeInput = "3\n10\n";
            clsInputValidate.setTestScanner(new java.util.Scanner(new java.io.ByteArrayInputStream(fakeInput.getBytes())));

            // Capture console output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            // Call method under test
            clsFindCDScreen.showFindCDScreen();

            // Get output
            String output = outputStream.toString();

            // Assertions
            assertTrue(output.contains("CD(s) Found:"));
            assertTrue(output.contains("Title   : ty"));
            assertTrue(output.contains("Artist  : tys"));
            assertTrue(output.contains("CD ID   : 10"));

        } finally {
            // Restore CDs.txt from backup
            if (Files.exists(backupFile)) {
                Files.copy(backupFile, cdsFile, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(backupFile);
            }

            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore system out
            System.setOut(System.out);
        }
    }

}
