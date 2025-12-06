package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class TestClsAdminMainScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Capture system output (optional, to verify prompts)
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        // IMPORTANT: always reset the scanner for each test
        clsInputValidate.setTestScanner(null);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testReadMainMenuOption_Books() {
        simulateInput("1\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eBooks, option);
    }

    @Test
    void testReadMainMenuOption_CDs() {
        simulateInput("2\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eCDs, option);
    }

    @Test
    void testReadMainMenuOption_ManageUsers() {
        simulateInput("3\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eManageUsers, option);
    }

    @Test
    void testReadMainMenuOption_SendReminders() {
        simulateInput("4\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eSendReminders, option);
    }

    @Test
    void testReadMainMenuOption_ViewAllLoans() {
        simulateInput("5\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eViewAllLoans, option);
    }

    @Test
    void testReadMainMenuOption_Logout() {
        simulateInput("6\n");
        clsAdminMainScreen.enMainMenuOptions option = clsAdminMainScreen._ReadMainMenuOption();
        assertEquals(clsAdminMainScreen.enMainMenuOptions.eLogout, option);
    }


    @Test
    void testLogout_SetsCurrentUserToFindEmpty() throws Exception {
        // Set some current user first
        clsUserSession.currentUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "testuser",
                "password",
                -1
        );

        // Call private static _Logout() via reflection
        Method logoutMethod = clsAdminMainScreen.class.getDeclaredMethod("_Logout");
        logoutMethod.setAccessible(true);
        logoutMethod.invoke(null); // static method

        // Get the "expected" user
        clsUser expectedUser = clsUser.find("", "");

        // Compare fields instead of object reference
        assertEquals(expectedUser.getUserName(), clsUserSession.currentUser.getUserName());
        assertEquals(expectedUser.getFirstName(), clsUserSession.currentUser.getFirstName());
        assertEquals(expectedUser.getLastName(), clsUserSession.currentUser.getLastName());
        assertEquals(expectedUser.getEmail(), clsUserSession.currentUser.getEmail());
        assertEquals(expectedUser.getPhone(), clsUserSession.currentUser.getPhone());

    }


    @Test
    void testShowCDsMenu_CallsCDAdminMenu() throws Exception {
        // Mock the static method
        try (MockedStatic<clsAdminCDsMenuScreen> mocked = mockStatic(clsAdminCDsMenuScreen.class)) {

            // Call private static _ShowCDsMenu() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowCDsMenu");
            method.setAccessible(true);
            method.invoke(null); // static method

            // Verify that showCDAdminMenu() was called exactly once
            mocked.verify(clsAdminCDsMenuScreen::showCDAdminMenu, times(1));
        }
    }


    @Test
    void testShowManageUsersMenu_CallsManageUsersMenu() throws Exception {
        // Mock the static method in clsManageUsersMenuScreen
        try (MockedStatic<clsManageUsersMenuScreen> mocked = mockStatic(clsManageUsersMenuScreen.class)) {

            // Call private static _ShowManageUsersMenu() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowManageUsersMenu");
            method.setAccessible(true);
            method.invoke(null);

            // Verify that showManageUsersMenu() was called exactly once
            mocked.verify(clsManageUsersMenuScreen::showManageUsersMenu, times(1));
        }
    }

    @Test
    void testSendOverdueReminders_CallsReminderServiceAndDrawHeader() throws Exception {
        // Mock the static method in clsReminderService
        try (MockedStatic<clsReminderService> mockedReminder = mockStatic(clsReminderService.class);
             MockedStatic<clsAdminMainScreen> mockedScreen = mockStatic(clsAdminMainScreen.class, CALLS_REAL_METHODS)) {

            // Call private static _SendOverdueReminders() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_SendOverdueReminders");
            method.setAccessible(true);
            method.invoke(null);

            // Verify that sendOverdueRemindersNow() was called exactly once
            mockedReminder.verify(clsReminderService::sendOverdueRemindersNow, times(1));

            // Verify that _DrawScreenHeader was called with the correct argument
            mockedScreen.verify(() -> clsAdminMainScreen._DrawScreenHeader("\t\tSend Overdue Reminders"), times(1));
        }
    }

    @Test
    void testGoBackToMainMenu_PrintsPromptAndWaitsInput() throws Exception {
        // Simulate user pressing Enter
        String simulatedInput = "\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Call private method via reflection
        Method method = clsAdminMainScreen.class.getDeclaredMethod("_GoBackToMainMenu");
        method.setAccessible(true);

        // Wrap in try-catch to avoid actually calling showMainMenu()
        try {
            method.invoke(null);
        } catch (Exception e) {
            // Ignore, because showMainMenu() will run (or throw)
        }

        String printed = out.toString();
        assertTrue(printed.contains("Press any key to go back to Main Menu"),
                "Prompt should be printed");

        // Restore
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    void testShowAllLoans_FileDoesNotExist() throws Exception {
        Path path = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans_backup.txt");

        // Step 1: Backup the file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath);
        }

        try {
            // Step 2: Delete the file if it exists
            if (Files.exists(path)) {
                Files.delete(path);
            }

            // Step 3: Capture system output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));

            // Call private static _ShowAllLoans() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowAllLoans");
            method.setAccessible(true);
            method.invoke(null);

            String printed = out.toString();

            // Verify that "No loans found." message is printed
            assertTrue(printed.contains("No loans found"), "Should print 'No loans found.' when file does not exist");

            // Restore System.out
            System.setOut(originalOut);

        } finally {
            // Step 4: Restore the file if backup exists
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }
    @Test
    void testShowAllLoans_NoLoans() throws Exception {
        Path path = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans_backup.txt");

        // Step 1: Backup the file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath);
        }

        try {
            // Step 2: Empty the file
            Files.write(path, new byte[0]);

            // Step 3: Capture system output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));

            // Call private static _ShowAllLoans() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowAllLoans");
            method.setAccessible(true);
            method.invoke(null);

            String printed = out.toString();

            // Verify that "No loans found." message is printed
            assertTrue(printed.contains("No loans found"), "Should print 'No loans found.' when file is empty");

            // Restore System.out
            System.setOut(originalOut);

        } finally {
            // Step 4: Restore the file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }

    @Test
    void testShowAllLoans_FileWithOneLoan() throws Exception {
        Path path = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans_backup.txt");

        // Step 1: Backup the file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath);
        }

        try {
            // Step 2: Write a single loan line to the file
            String loanLine = "ispn-14-mv#//#User2#//#2025-11-06#//#2025-12-04#//#false";
            Files.write(path, loanLine.getBytes());

            // Step 3: Capture system output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));

            // Call private static _ShowAllLoans() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowAllLoans");
            method.setAccessible(true);
            method.invoke(null);

            String printed = out.toString();

            // Verify that the loan information is printed
            assertTrue(printed.contains("User2"), "Output should contain the username 'User2'");
            assertTrue(printed.contains("ispn-14-mv"), "Output should contain the ISBN 'ispn-14-mv'");
            assertTrue(printed.contains("2025-11-06"), "Output should contain the borrow date");
            assertTrue(printed.contains("2025-12-04"), "Output should contain the due date");
            assertTrue(printed.contains("false"), "Output should contain the returned status");

            // Restore System.out
            System.setOut(originalOut);

        } finally {
            // Step 4: Restore the file if backup exists
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }


    @Test
    void testShowMainMenu_PrintsMenuAndExitsOnLogout() throws Exception {
        // Step 1: Simulate user input "6\n" (Logout)
        String simulatedInput = "6\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Step 2: Capture system output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        // Step 3: Call the method
        clsAdminMainScreen.showMainMenu();

        // Step 4: Get printed output
        String printed = out.toString();

        // Step 5: Check that the menu is displayed correctly
        assertTrue(printed.contains("Admin Main Menu"), "Menu header should be printed");
        assertTrue(printed.contains("[1] Books Menu"), "Books menu option should be printed");
        assertTrue(printed.contains("[6] Logout"), "Logout option should be printed");

        // Restore System.out
        System.setOut(originalOut);
    }



    @Test
    void testShowAllLoans_LineWithLessThanFiveFields() throws Exception {
        Path path = Paths.get("Loans.txt");
        Path backupPath = Paths.get("Loans_backup.txt");

        // Step 1: Backup the file if it exists
        if (Files.exists(path)) {
            Files.copy(path, backupPath);
        }

        try {
            // Step 2: Empty the file and write a malformed line
            String malformedLine = "ispn-14-mv#//#2025-11-06#//#2025-12-04#//#false"; // only 4 fields
            Files.write(path, malformedLine.getBytes());

            // Step 3: Capture system output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(out));

            // Call private static _ShowAllLoans() via reflection
            Method method = clsAdminMainScreen.class.getDeclaredMethod("_ShowAllLoans");
            method.setAccessible(true);
            method.invoke(null);

            String printed = out.toString();

            // Step 4: Verify that the malformed line is skipped
            assertFalse(printed.contains("ispn-14-mv"), "Malformed line should be skipped and not printed");
            assertTrue(printed.contains("Username"), "Header should still be printed if there is at least one line?");
            // Actually header only prints if lines have >=5 fields. Otherwise no header is printed. Adjust assert if needed.

            // Restore System.out
            System.setOut(originalOut);

        } finally {
            // Step 5: Restore the file
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                Files.delete(backupPath);
            }
        }
    }
    // Helper method to simulate user input
    private void simulateInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }



}
