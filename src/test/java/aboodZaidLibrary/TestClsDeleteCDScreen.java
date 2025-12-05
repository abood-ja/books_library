package aboodZaidLibrary;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
class TestClsDeleteCDScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        if (clsUserSession.currentUser == null){
        clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
        clsUserSession.currentUser.setFirstName("Test");
        clsUserSession.currentUser.setLastName("User");}

        // Reset input scanner
        clsInputValidate.setTestScanner(null);
    }
    @BeforeEach
    void setUpStreams() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void test_PrintCD() throws Exception {
        // Create a sample CD using your constructor
        clsCD cd = new clsCD(clsCD.enMode.AddNewMode, "Test Title", "Test Artist", "123");

        // Access private method using reflection
        Method printCDMethod = clsDeleteCDScreen.class.getDeclaredMethod("_PrintCD", clsCD.class);
        printCDMethod.setAccessible(true);

        // Invoke the private method
        printCDMethod.invoke(null, cd);

        // Capture output and assert
        String output = outputStream.toString();
        assertTrue(output.contains("CD Details:"));
        assertTrue(output.contains("Title   : Test Title"));
        assertTrue(output.contains("Artist  : Test Artist"));
        assertTrue(output.contains("CD ID   : 123"));
    }
    @Test
    void test_showDeleteCDScreen_NoPermission() {
        // 1. Explicitly set a user with no delete permission
        clsUserSession.currentUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "testuser",
                "password",
                0 // no permissions
        );

        // 2. Set a dummy scanner to avoid hanging if input is requested
        clsInputValidate.setTestScanner(new Scanner(new ByteArrayInputStream("".getBytes())));

        // 3. Call the method
        clsDeleteCDScreen.showDeleteCDScreen();

        // 4. Capture the output
        String output = outputStream.toString();

        // 5. Assert that nothing from the delete flow is printed
        assertFalse(output.contains("Delete CD"));
        assertFalse(output.contains("Enter CD ID"));
        assertFalse(output.contains("CD Deleted Successfully!"));

        // 6. Reset test scanner
        clsInputValidate.setTestScanner(null);
    }

    @Test
    void test_showDeleteCDScreen_WithAccess_ConfirmYes() throws Exception {
        // 1. Backup the file
        File cdsFile = new File("CDs.txt");
        File backupFile = new File("CDs_backup.txt");
        if (cdsFile.exists()) {
            Files.copy(cdsFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty the file
            PrintWriter writer = new PrintWriter(cdsFile);
            writer.close();

            // 3. Add a test CD line (simulate storage)
            try (FileWriter fw = new FileWriter(cdsFile, true)) {
                fw.write("soft#//#zaid#//#soft1234\n");
            }

            // 4. Create a user with delete permission
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "testuser",
                    "password",
                    clsUser.enPermissions.pDeleteBook.getValue()
            );

            // 5. Simulate user input: CD ID, then 'y' to confirm
            String simulatedInput = "soft1234\ny\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 6. Call the method
            clsDeleteCDScreen.showDeleteCDScreen();

            // 7. Capture output and assert
            String output = outputStream.toString();
            assertTrue(output.contains("CD Details:"));
            assertTrue(output.contains("Title   : soft"));
            assertTrue(output.contains("Artist  : zaid"));
            assertTrue(output.contains("CD ID   : soft1234"));
            assertTrue(output.contains("CD Deleted Successfully"));

            // 8. Verify the CD was deleted
            String fileContent = String.join("\n", Files.readAllLines(cdsFile.toPath()));
            assertFalse(fileContent.contains("soft#//#zaid#//#soft1234"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore original file
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), cdsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                backupFile.delete();
            }
        }
    }

    @Test
    void test_showDeleteCDScreen_WithAccess_ConfirmNo() throws Exception {
        // 1. Backup the file
        File cdsFile = new File("CDs.txt");
        File backupFile = new File("CDs_backup.txt");
        if (cdsFile.exists()) {
            Files.copy(cdsFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // 2. Empty the file
            PrintWriter writer = new PrintWriter(cdsFile);
            writer.close();

            // 3. Add a test CD line (simulate storage)
            try (FileWriter fw = new FileWriter(cdsFile, true)) {
                fw.write("soft#//#zaid#//#soft1234\n");
            }

            // 4. Create a user with delete permission
            clsUserSession.currentUser = new clsUser(
                    clsUser.enMode.AddNewMode,
                    "Test",
                    "User",
                    "test@example.com",
                    "1234567890",
                    "testuser",
                    "password",
                    clsUser.enPermissions.pDeleteBook.getValue()
            );

            // 5. Simulate user input: CD ID, then 'n' to cancel
            String simulatedInput = "soft1234\nn\n";
            Scanner testScanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
            clsInputValidate.setTestScanner(testScanner);

            // 6. Call the method
            clsDeleteCDScreen.showDeleteCDScreen();

            // 7. Capture output
            String output = outputStream.toString();
            assertTrue(output.contains("CD Details:"));
            assertTrue(output.contains("Title   : soft"));
            assertTrue(output.contains("Artist  : zaid"));
            assertTrue(output.contains("CD ID   : soft1234"));
            // Confirm that deletion message does NOT appear
            assertFalse(output.contains("CD Deleted Successfully"));

            // 8. Verify the CD was NOT deleted
            String fileContent = String.join("\n", Files.readAllLines(cdsFile.toPath()));
            assertTrue(fileContent.contains("soft#//#zaid#//#soft1234"));

        } finally {
            // Reset test scanner
            clsInputValidate.setTestScanner(null);

            // Restore original file
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), cdsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                backupFile.delete();
            }
        }
    }

}
