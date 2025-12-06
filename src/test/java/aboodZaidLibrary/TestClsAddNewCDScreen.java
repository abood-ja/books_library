package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TestClsAddNewCDScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private Path backupPath = Paths.get("CDs_backup.txt");

    @BeforeEach
    void setUp() throws IOException {
        // Backup the original file
        Path originalPath = Paths.get("CDs.txt");
        if (Files.exists(originalPath)) {
            Files.copy(originalPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Capture console output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create a user with full permission
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

        // Restore the original file
        Path originalPath = Paths.get("CDs.txt");
        if (Files.exists(backupPath)) {
            Files.copy(backupPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backupPath);
        }

        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null); // Reset test scanner
    }

    @Test
    void testAddNewCDSuccessfully() throws IOException {
        Files.write(Paths.get("CDs.txt"), new byte[0]);
        String simulatedInput = "Best Hits\nArtist Name\nCD123\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("✅ CD added successfully"));

        String content = new String(Files.readAllBytes(Paths.get("CDs.txt")));
        assertTrue(content.contains("Best Hits#//#Artist Name#//#CD123"));
    }

    @Test
    void testAddCDWithExistingID() throws IOException {
        Files.write(Paths.get("CDs.txt"), "Old Album#//#Old Artist#//#CD123\n".getBytes());

        String simulatedInput = "New Album\nNew Artist\nCD123\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("⚠️ A CD with this ID already exists!"));
    }

    @Test
    void testUserWithoutPermission() {
        clsUserSession.currentUser.setPermissions(0);

        String simulatedInput = "Album\nArtist\nCD001\n";
        clsInputValidate.setTestScanner(
                new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
        );

        clsAddNewCDScreen.showAddNewCDScreen();

        String output = outContent.toString();
        assertTrue(output.contains("❌ You do not have permission to add CDs."));
    }


    // ===== New tests for __ensureCDsFile() =====

    @Test
    void testEnsureCDsFile_FileDoesNotExist() throws Exception {
        Path filePath = Paths.get("CDs.txt");
        if (Files.exists(filePath)) Files.delete(filePath); // ensure file is missing

        // call private static method via reflection
        Method method = clsAddNewCDScreen.class.getDeclaredMethod("__ensureCDsFile");
        method.setAccessible(true);
        method.invoke(null);

        assertTrue(Files.exists(filePath), "CDs.txt should be created if it does not exist");
    }

    @Test
    void testEnsureCDsFile_FileExistsButEmpty() throws Exception {
        Path filePath = Paths.get("CDs.txt");
        Files.write(filePath, new byte[0]); // empty the file

        Method method = clsAddNewCDScreen.class.getDeclaredMethod("__ensureCDsFile");
        method.setAccessible(true);
        method.invoke(null);

        assertTrue(Files.exists(filePath), "CDs.txt should still exist after ensuring");
        assertEquals(0, Files.size(filePath), "CDs.txt should remain empty");
    }


    @Test
    void testCdIDExists_FileDoesNotExist() throws Exception {
        Path filePath = Paths.get("CDs.txt");

        // Backup and delete
        if (Files.exists(filePath)) Files.delete(filePath);

        // Call private static method via reflection
        Method method = clsAddNewCDScreen.class.getDeclaredMethod("__cdIDExists", String.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(null, "CD123");

        // If file doesn't exist, method should return false
        assertFalse(result, "Method should return false if file does not exist");
    }

    @Test
    void testCdIDExists_FileEmpty() throws Exception {
        Path filePath = Paths.get("CDs.txt");

        // Backup and empty the file
        Files.write(filePath, new byte[0]);

        Method method = clsAddNewCDScreen.class.getDeclaredMethod("__cdIDExists", String.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(null, "CD123");

        // Empty file → ID cannot exist
        assertFalse(result, "Method should return false if file is empty");
    }

    @Test
    void testCdIDExists_WhenCDExists() throws Exception {
        Path filePath = Paths.get("CDs.txt");

        // Backup the original file
        Path backupPath = Paths.get("CDs_backup.txt");
        if (Files.exists(filePath)) {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file and write a CD line
            Files.write(filePath, "ty#//#ty#//#10\n".getBytes());

            // Call private static method via reflection
            Method method = clsAddNewCDScreen.class.getDeclaredMethod("__cdIDExists", String.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "10");

            assertTrue(result, "Method should return true when CD with given ID exists");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }


    @Test
    void testAppendCD_Success() throws Exception {
        Path filePath = Paths.get("CDs.txt");
        Path backupPath = Paths.get("CDs_backup.txt");

        // Backup the original file
        if (Files.exists(filePath)) {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Call private static method via reflection
            Method method = clsAddNewCDScreen.class.getDeclaredMethod("__appendCD", String.class, String.class, String.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "ty", "ty", "10");

            assertTrue(result, "Method should return true on successful append");

            // Check if the file contains the expected line
            String content = Files.readAllLines(filePath).get(0);
            assertEquals("ty#//#ty#//#10", content, "CD line should be correctly written to the file");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }

    @Test
    void testShowAddNewCDScreen_UserWithoutPermission() {
        // Set current user with no permissions
        clsUserSession.currentUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "qbr",
                "password",
                0 // no permissions
        );

        // Call the method
        clsAddNewCDScreen.showAddNewCDScreen();

        // Capture output
        String output = outContent.toString();

        // Check that the permission warning is printed
        assertTrue(output.contains("❌ You do not have permission to add CDs."),
                "User without permission should be blocked from adding CDs");
    }


    @Test
    void testShowAddNewCDScreen_AddCDSuccessfully() throws IOException {
        Path filePath = Paths.get("CDs.txt");
        Path backupPath = Paths.get("CDs_backup.txt");

        // Backup the original file
        if (Files.exists(filePath)) {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Ensure current user has permissions
            clsUserSession.currentUser.setPermissions(clsUser.enPermissions.pAddNewBook.getValue());

            // Simulate user input for title, artist, and ID
            String simulatedInput = "title\nauthor\n12\n";
            clsInputValidate.setTestScanner(
                    new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
            );

            // Call the method
            clsAddNewCDScreen.showAddNewCDScreen();

            // Capture output
            String output = outContent.toString();

            // Check that the success message is printed
            assertTrue(output.contains("✅ CD added successfully."),
                    "The success message should be printed when CD is added");

            // Check that the CD was written to the file
            String content = Files.readAllLines(filePath).get(0);
            assertEquals("title#//#author#//#12", content, "The CD should be correctly written to the file");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }

    @Test
    void testAppendCD_Failure_ReadOnly() throws Exception {
        Path filePath = Paths.get("CDs.txt");
        Files.write(filePath, new byte[0]);
        filePath.toFile().setReadOnly(); // make it read-only

        try {
            Method method = clsAddNewCDScreen.class.getDeclaredMethod("__appendCD", String.class, String.class, String.class);
            method.setAccessible(true);

            boolean result = (boolean) method.invoke(null, "title", "artist", "id");
            assertFalse(result, "Method should return false when writing to a read-only file");

        } finally {
            filePath.toFile().setWritable(true); // restore write permission
        }
    }

    @Test
    void testShowAddNewCDScreen_AppendFails() throws IOException {
        Path filePath = Paths.get("CDs.txt");
        Path backupPath = Paths.get("CDs_backup.txt");

        // Backup the original file
        if (Files.exists(filePath)) {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file
            Files.write(filePath, new byte[0]);

            // Make file read-only to simulate append failure
            filePath.toFile().setReadOnly();

            // Ensure current user has permissions
            clsUserSession.currentUser.setPermissions(clsUser.enPermissions.pAddNewBook.getValue());

            // Simulate user input for title, artist, and ID
            String simulatedInput = "title\nauthor\n12\n";
            clsInputValidate.setTestScanner(
                    new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
            );

            // Call the method
            clsAddNewCDScreen.showAddNewCDScreen();

            // Capture output
            String output = outContent.toString();

            // Since append fails, success message should NOT be printed
            assertFalse(output.contains("✅ CD added successfully."),
                    "Success message should NOT be printed if append fails");

            // Check that the file is still empty (nothing was written)
            assertEquals(0, Files.size(filePath), "File should remain empty if append fails");

        } finally {
            // Restore write permission and original file
            filePath.toFile().setWritable(true);
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }


    @Test
    void testShowAddNewCDScreen_CDAlreadyExists() throws IOException {
        Path filePath = Paths.get("CDs.txt");
        Path backupPath = Paths.get("CDs_backup.txt");

        // Backup the original file
        if (Files.exists(filePath)) {
            Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Empty the file and write an existing CD
            Files.write(filePath, "ty#//#ty#//#10\n".getBytes());

            // Ensure current user has permissions
            clsUserSession.currentUser.setPermissions(clsUser.enPermissions.pAddNewBook.getValue());

            // Simulate user input: title, artist, and duplicate ID
            String simulatedInput = "title\nauthor\n10\n";
            clsInputValidate.setTestScanner(
                    new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()))
            );

            // Call the method
            clsAddNewCDScreen.showAddNewCDScreen();

            // Capture output
            String output = outContent.toString();

            // Check that the duplicate ID message is printed
            assertTrue(output.contains("⚠️ A CD with this ID already exists!"),
                    "The method should warn when a CD with the same ID already exists");

            // Ensure the file still only contains the original line
            String content = Files.readAllLines(filePath).get(0);
            assertEquals("ty#//#ty#//#10", content, "The original CD should remain, no duplicate added");

        } finally {
            // Restore the original file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, filePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }


}
