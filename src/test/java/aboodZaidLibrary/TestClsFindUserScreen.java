package aboodZaidLibrary;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class TestClsFindUserScreen {

    @Test
    void testPrintUser() throws Exception {

        // Arrange
        clsUser user = new clsUser(
                clsUser.enMode.UpdateMode,
                "abood",
                "jarrar",
                "abood@gmail.com",
                "0599",
                "user1",
                "1234",
                -1
        );

        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call private static method
        Method method = clsFindUserScreen.class.getDeclaredMethod("_PrintUser", clsUser.class);
        method.setAccessible(true);
        method.invoke(null, user);

        // Get printed output
        String output = outContent.toString();

        // Assertions — check that output contains the important details
        assertTrue(output.contains("User Details"));
        assertTrue(output.contains("abood"));
        assertTrue(output.contains("jarrar"));
        assertTrue(output.contains(user.getFullName()));
        assertTrue(output.contains("abood@gmail.com"));
        assertTrue(output.contains("user1"));
        assertTrue(output.contains("1234"));
        assertTrue(output.contains("-1"));
    }
    @Test
    void testShowFindUserScreen_UserExists() throws Exception {
        // Paths for Users.txt and backup
        java.nio.file.Path usersFile = java.nio.file.Paths.get("Users.txt");
        java.nio.file.Path backupFile = java.nio.file.Paths.get("Users_backup.txt");

        // Backup the original Users.txt if it exists
        if (java.nio.file.Files.exists(usersFile)) {
            java.nio.file.Files.copy(usersFile, backupFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Overwrite Users.txt with the test user
            String testUserLine = "abood#//#jarrar#//#abood@gmail.com#//#0599#//#user1#//#1234#//#-1\n";
            java.nio.file.Files.write(usersFile, testUserLine.getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);

            // Simulate user input
            String simulatedInput = "user1\n";
            System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

            // Capture system output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outContent));

            // Call method under test
            clsFindUserScreen.showFindUserScreen();

            // Restore System.out
            System.setOut(originalOut);

            // Get output
            String output = outContent.toString();

            // Assertions
            assertTrue(output.contains("Find User Screen"));
            assertTrue(output.contains("Please enter Account Username"));
            assertTrue(output.contains("User Found"));
            assertTrue(output.contains("abood")); // first name
            assertTrue(output.contains("jarrar")); // last name
            assertTrue(output.contains("abood@gmail.com")); // email

        } finally {
            // Restore original Users.txt
            if (java.nio.file.Files.exists(backupFile)) {
                java.nio.file.Files.copy(backupFile, usersFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                java.nio.file.Files.delete(backupFile);
            } else {
                java.nio.file.Files.deleteIfExists(usersFile);
            }

            // Reset System.in
            System.setIn(System.in);
        }
    }

}
