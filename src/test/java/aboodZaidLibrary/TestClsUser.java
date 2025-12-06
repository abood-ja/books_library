package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.util.Vector;

/**
 * SAFE TEST CLASS for clsUser
 * This test class creates BACKUP copies of your files before testing
 * and restores them after each test to prevent data loss.
 */
class TestClsUser {

    // Backup file names
    private static final String USERS_FILE = "Users.txt";
    private static final String LOGIN_FILE = "LoginRegister.txt";
    private static final String LOANS_FILE = "Loans.txt";

    private static final String USERS_BACKUP = "Users_BACKUP.txt";
    private static final String LOGIN_BACKUP = "LoginRegister_BACKUP.txt";
    private static final String LOANS_BACKUP = "Loans_BACKUP.txt";

    // Test data
    private clsUser testUser;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("⚠️  CREATING BACKUPS OF YOUR DATA FILES...");

        // Create backups of existing files
        backupFile(USERS_FILE, USERS_BACKUP);
        backupFile(LOGIN_FILE, LOGIN_BACKUP);
        backupFile(LOANS_FILE, LOANS_BACKUP);

        System.out.println("✅ Backups created successfully!");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("⚠️  RESTORING YOUR ORIGINAL DATA FILES...");

        // Restore original files from backup
        restoreFile(USERS_BACKUP, USERS_FILE);
        restoreFile(LOGIN_BACKUP, LOGIN_FILE);
        restoreFile(LOANS_BACKUP, LOANS_FILE);

        // Delete backup files
        deleteFileIfExists(USERS_BACKUP);
        deleteFileIfExists(LOGIN_BACKUP);
        deleteFileIfExists(LOANS_BACKUP);

        System.out.println("✅ Original files restored successfully!");
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create a fresh test user for each test
        testUser = new clsUser(
                clsUser.enMode.AddNewMode,
                "Test",
                "User",
                "test@example.com",
                "1234567890",
                "testuser_" + System.currentTimeMillis(), // Unique username
                "testpass123",
                1
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up: restore files after each test
        restoreFile(USERS_BACKUP, USERS_FILE);
        restoreFile(LOGIN_BACKUP, LOGIN_FILE);
        restoreFile(LOANS_BACKUP, LOANS_FILE);
    }

    // ===== Helper Methods =====

    private static void backupFile(String source, String backup) {
        try {
            Path sourcePath = Paths.get(source);
            if (Files.exists(sourcePath)) {
                Files.copy(sourcePath, Paths.get(backup), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.out.println("Note: Could not backup " + source + " (file may not exist yet)");
        }
    }

    private static void restoreFile(String backup, String target) {
        try {
            Path backupPath = Paths.get(backup);
            if (Files.exists(backupPath)) {
                Files.copy(backupPath, Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // If no backup exists, delete the target file
                deleteFileIfExists(target);
            }
        } catch (IOException e) {
            System.out.println("Note: Could not restore " + target);
        }
    }

    private static void deleteFileIfExists(String filename) {
        try {
            Files.deleteIfExists(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Could not delete " + filename);
        }
    }

    // ===== Constructor Tests =====

    @Test
    void testConstructor_AllParameters() {
        // Arrange & Act
        clsUser user = new clsUser(
                clsUser.enMode.UpdateMode,
                "John",
                "Doe",
                "john@example.com",
                "1234567890",
                "johndoe",
                "password123",
                clsUser.enPermissions.pAddNewBook.getValue()
        );

        // Assert
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals("johndoe", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertEquals(clsUser.enPermissions.pAddNewBook.getValue(), user.getPermissions());
    }

    // ===== Enum Tests =====

    @Test
    void testEnumMode_Values() {
        assertEquals(0, clsUser.enMode.EmptyMode.getValue());
        assertEquals(1, clsUser.enMode.UpdateMode.getValue());
        assertEquals(2, clsUser.enMode.AddNewMode.getValue());
    }

    @Test
    void testEnumPermissions_Values() {
        assertEquals(-1, clsUser.enPermissions.eAll.getValue());
        assertEquals(1, clsUser.enPermissions.pAddNewBook.getValue());
        assertEquals(2, clsUser.enPermissions.pDeleteBook.getValue());
        assertEquals(4, clsUser.enPermissions.pManageUsers.getValue());
    }

    @Test
    void testEnumSaveResults_Values() {
        assertEquals(0, clsUser.enSaveResults.svFaildEmptyObject.getValue());
        assertEquals(1, clsUser.enSaveResults.svSucceeded.getValue());
        assertEquals(2, clsUser.enSaveResults.svFaildUserExists.getValue());
    }

    // ===== Getters and Setters Tests =====

    @Test
    void testSetGetUserName() {
        testUser.setUserName("newusername");
        assertEquals("newusername", testUser.getUserName());
    }

    @Test
    void testSetGetPassword() {
        testUser.setPassword("newpassword");
        assertEquals("newpassword", testUser.getPassword());
    }

    @Test
    void testSetGetPermissions() {
        testUser.setPermissions(4);
        assertEquals(4, testUser.getPermissions());
    }

    // ===== isEmpty Tests =====

    @Test
    void testIsEmpty_EmptyMode() {
        clsUser emptyUser = new clsUser(
                clsUser.enMode.EmptyMode,
                "", "", "", "", "", "", 0
        );
        assertTrue(emptyUser.isEmpty());
    }

    @Test
    void testIsEmpty_UpdateMode() {
        clsUser user = new clsUser(
                clsUser.enMode.UpdateMode,
                "Test", "User", "test@test.com", "123",
                "testuser", "pass", 1
        );
        assertFalse(user.isEmpty());
    }

    // ===== isAdmin Tests =====

    @Test
    void testIsAdmin_AdminUser() {
        testUser.setPermissions(-1);
        assertTrue(testUser.isAdmin());
    }

    @Test
    void testIsAdmin_NonAdminUser() {
        testUser.setPermissions(1);
        assertFalse(testUser.isAdmin());
    }

    // ===== Save Tests =====




    // ===== Find Tests =====


    @Test
    void testFind_ByUsername_UserNotExists() {
        // Act
        clsUser foundUser = clsUser.find("nonexistentuser_12345");

        // Assert
        assertTrue(foundUser.isEmpty());
    }


    @Test
    void testFind_ByUsernameAndPassword_WrongPassword() {
        // Arrange
        String username = "authuser2_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("correctpass");
        user.save();

        // Act
        clsUser foundUser = clsUser.find(username, "wrongpass");

        // Assert
        assertTrue(foundUser.isEmpty());
    }

    // ===== isUserExist Tests =====


    @Test
    void testIsUserExist_UserNotExists() {
        // Act & Assert
        assertFalse(clsUser.isUserExist("nonexistent_" + System.currentTimeMillis()));
    }

    // ===== GetAddNewUserObject Test =====

    @Test
    void testGetAddNewUserObject() {
        // Act
        clsUser newUser = clsUser.GetAddNewUserObject("newuser123");

        // Assert
        assertEquals("newuser123", newUser.getUserName());
        assertFalse(newUser.isEmpty());
    }

    // ===== getUsersList Tests =====


    // ===== Delete Tests =====


    @Test
    void testIsMarkedForDeleted() {
        // Initially false
        assertFalse(testUser.isMarkedForDeleted());
    }

    // ===== checkAccessPermission Tests =====

    @Test
    void testCheckAccessPermission_AdminHasAllPermissions() {
        testUser.setPermissions(clsUser.enPermissions.eAll.getValue());

        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pAddNewBook));
        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pDeleteBook));
        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pManageUsers));
    }

    @Test
    void testCheckAccessPermission_UserHasSpecificPermission() {
        testUser.setPermissions(clsUser.enPermissions.pAddNewBook.getValue());

        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pAddNewBook));
        assertFalse(testUser.checkAccessPermission(clsUser.enPermissions.pDeleteBook));
    }

    @Test
    void testCheckAccessPermission_UserHasMultiplePermissions() {
        // Set multiple permissions using bitwise OR
        int permissions = clsUser.enPermissions.pAddNewBook.getValue()
                | clsUser.enPermissions.pDeleteBook.getValue();
        testUser.setPermissions(permissions);

        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pAddNewBook));
        assertTrue(testUser.checkAccessPermission(clsUser.enPermissions.pDeleteBook));
        assertFalse(testUser.checkAccessPermission(clsUser.enPermissions.pManageUsers));
    }

    // ===== registerLogin Test =====




    @Test
    void testRegisterLogin() {
        // Arrange
        String username = "loginuser_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("loginpass");
        user.save();

        // Act
        user.registerLogin();

        // Assert - verify login was registered
        Vector<clsUser.clsLoginRegisterRecord> records = clsUser.getLoginRegisterList();
        assertNotNull(records);

        boolean found = false;
        for (clsUser.clsLoginRegisterRecord rec : records) {
            if (rec.userName.equals(username)) {
                found = true;
                assertEquals("loginpass", rec.password);
                break;
            }
        }
        assertTrue(found);
    }

    // ===== getLoginRegisterList Test =====

    @Test
    void testGetLoginRegisterList() {
        // Act
        Vector<clsUser.clsLoginRegisterRecord> records = clsUser.getLoginRegisterList();

        // Assert
        assertNotNull(records);
    }

    // Add these tests to your TestClsUser class
// These tests work with the existing backup system:
// - @BeforeAll creates backups of Users.txt, LoginRegister.txt, Loans.txt
// - @AfterEach restores files after each test
// - @AfterAll cleans up backups at the end

    @Test
    void testSave_UpdateMode_Success() {
        // Arrange - First create and save a user
        String username = "updatetest_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setFirstName("Original");
        user.setLastName("Name");
        user.setPassword("pass123");
        user.setPermissions(1);

        clsUser.enSaveResults saveResult = user.save();
        assertEquals(clsUser.enSaveResults.svSucceeded, saveResult);

        // Act - Now find the user and update it
        clsUser foundUser = clsUser.find(username);
        assertFalse(foundUser.isEmpty());

        // Modify the user
        foundUser.setFirstName("Updated");
        foundUser.setLastName("NewName");
        foundUser.setPassword("newpass456");
        foundUser.setPermissions(3);

        // Save the update (this will call _Update())
        clsUser.enSaveResults updateResult = foundUser.save();

        // Assert
        assertEquals(clsUser.enSaveResults.svSucceeded, updateResult);

        // Verify the changes were saved
        clsUser verifyUser = clsUser.find(username);
        assertEquals("Updated", verifyUser.getFirstName());
        assertEquals("NewName", verifyUser.getLastName());
        assertEquals("newpass456", verifyUser.getPassword());
        assertEquals(3, verifyUser.getPermissions());
    }

    @Test
    void testSave_UpdateMode_UpdatesCorrectUser() {
        // Arrange - Create multiple users
        String username1 = "user1_" + System.currentTimeMillis();
        String username2 = "user2_" + System.currentTimeMillis();

        clsUser user1 = clsUser.GetAddNewUserObject(username1);
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPassword("pass1");
        user1.save();

        clsUser user2 = clsUser.GetAddNewUserObject(username2);
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPassword("pass2");
        user2.save();

        // Act - Update user1
        clsUser foundUser1 = clsUser.find(username1);
        foundUser1.setFirstName("Modified");
        foundUser1.save();

        // Assert - Verify only user1 was updated
        clsUser verifyUser1 = clsUser.find(username1);
        assertEquals("Modified", verifyUser1.getFirstName());

        clsUser verifyUser2 = clsUser.find(username2);
        assertEquals("User", verifyUser2.getFirstName()); // Should remain unchanged
        assertEquals("Two", verifyUser2.getLastName());
    }

    @Test
    void testSave_AddNewMode_Success() {
        // Arrange
        String username = "newuser_" + System.currentTimeMillis();
        clsUser newUser = clsUser.GetAddNewUserObject(username);
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setPassword("password");
        newUser.setPermissions(2);

        // Act
        clsUser.enSaveResults result = newUser.save();

        // Assert
        assertEquals(clsUser.enSaveResults.svSucceeded, result);

        // Verify user exists
        assertTrue(clsUser.isUserExist(username));

        // Verify user can be found
        clsUser foundUser = clsUser.find(username);
        assertFalse(foundUser.isEmpty());
        assertEquals("New", foundUser.getFirstName());
    }

    @Test
    void testSave_AddNewMode_UserAlreadyExists() {
        // Arrange - Create a user first
        String username = "duplicate_" + System.currentTimeMillis();
        clsUser user1 = clsUser.GetAddNewUserObject(username);
        user1.setPassword("pass1");
        user1.save();

        // Act - Try to create another user with same username
        clsUser user2 = clsUser.GetAddNewUserObject(username);
        user2.setPassword("pass2");
        clsUser.enSaveResults result = user2.save();

        // Assert
        assertEquals(clsUser.enSaveResults.svFaildUserExists, result);
    }

    @Test
    void testSave_EmptyMode_Fails() {
        // Arrange - Create an empty user
        clsUser emptyUser = new clsUser(
                clsUser.enMode.EmptyMode,
                "", "", "", "", "", "", 0
        );

        // Act
        clsUser.enSaveResults result = emptyUser.save();

        // Assert
        assertEquals(clsUser.enSaveResults.svFaildEmptyObject, result);
    }

    @Test
    void testFind_ByUsername_UserExists() {
        // Arrange
        String username = "findtest_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setFirstName("Find");
        user.setLastName("Test");
        user.setPassword("findpass");
        user.setPermissions(1);
        user.save();

        // Act
        clsUser foundUser = clsUser.find(username);

        // Assert
        assertFalse(foundUser.isEmpty());
        assertEquals(username, foundUser.getUserName());
        assertEquals("Find", foundUser.getFirstName());
        assertEquals("Test", foundUser.getLastName());
        assertEquals("findpass", foundUser.getPassword());
    }


    @Test
    void testCanBeDeleted_NoLoansFile() throws IOException {
        // Arrange
        String username = "noloansfile_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("pass");
        user.save();

        // Backup Loans.txt if it exists
        Path loansFile = Paths.get("Loans.txt");
        Path loansBackup = Paths.get("Loans_TEMP_BACKUP.txt");
        boolean loansExisted = Files.exists(loansFile);

        if (loansExisted) {
            Files.copy(loansFile, loansBackup, StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            // Delete Loans.txt to test the "no file" scenario
            Files.deleteIfExists(loansFile);

            // Act
            boolean canDelete = user.canBeDeleted();

            // Assert - Should return true when no Loans.txt exists
            assertTrue(canDelete);

        } finally {
            // Restore Loans.txt if it existed
            if (loansExisted && Files.exists(loansBackup)) {
                Files.copy(loansBackup, loansFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Clean up temp backup
            Files.deleteIfExists(loansBackup);
        }
    }
    @Test
    void testFind_ByUsernameAndPassword_Success() {
        // Arrange
        String username = "authuser_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("correctpass");
        user.save();

        // Act
        clsUser foundUser = clsUser.find(username, "correctpass");

        // Assert
        assertFalse(foundUser.isEmpty());
        assertEquals(username, foundUser.getUserName());
        assertEquals("correctpass", foundUser.getPassword());
    }

    @Test
    void testIsUserExist_UserExists() {
        // Arrange
        String username = "existtest_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.save();

        // Act & Assert
        assertTrue(clsUser.isUserExist(username));
    }

    @Test
    void testGetUsersList() {
        // Arrange - Create a few users
        String username1 = "listuser1_" + System.currentTimeMillis();
        String username2 = "listuser2_" + System.currentTimeMillis();

        clsUser user1 = clsUser.GetAddNewUserObject(username1);
        user1.save();

        clsUser user2 = clsUser.GetAddNewUserObject(username2);
        user2.save();

        // Act
        Vector<clsUser> usersList = clsUser.getUsersList();

        // Assert
        assertNotNull(usersList);
        assertTrue(usersList.size() >= 2);

        // Verify our test users are in the list
        boolean foundUser1 = false;
        boolean foundUser2 = false;

        for (clsUser u : usersList) {
            if (u.getUserName().equals(username1)) foundUser1 = true;
            if (u.getUserName().equals(username2)) foundUser2 = true;
        }

        assertTrue(foundUser1);
        assertTrue(foundUser2);
    }
    // ===== Fine Management Tests =====

    @Test
    void testGetFineBalance_InitiallyZero() {
        assertEquals(0.0, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testAddFine() {
        testUser.addFine(50.0);
        assertEquals(50.0, testUser.getFineBalance(), 0.001);

        testUser.addFine(25.5);
        assertEquals(75.5, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testPayFine_PartialPayment() {
        testUser.addFine(100.0);
        testUser.payFine(30.0);
        assertEquals(70.0, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testPayFine_FullPayment() {
        testUser.addFine(50.0);
        testUser.payFine(50.0);
        assertEquals(0.0, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testPayFine_Overpayment() {
        testUser.addFine(50.0);
        testUser.payFine(100.0);
        assertEquals(0.0, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testPayFine_NegativeAmount() {
        testUser.addFine(50.0);
        testUser.payFine(-10.0);
        assertEquals(50.0, testUser.getFineBalance(), 0.001);
    }

    @Test
    void testPayFine_ZeroAmount() {
        testUser.addFine(50.0);
        testUser.payFine(0.0);
        assertEquals(50.0, testUser.getFineBalance(), 0.001);
    }

    // ===== hasUnpaidFines Tests =====

    @Test
    void testHasUnpaidFines_NoFines() {
        assertFalse(testUser.hasUnpaidFines());
    }

    @Test
    void testHasUnpaidFines_WithFines() {
        testUser.addFine(10.0);
        assertTrue(testUser.hasUnpaidFines());
    }

    // ===== hasOverdue Tests =====

    @Test
    void testHasOverdue_NoLoansFile() {
        // If Loans.txt doesn't exist, hasOverdue should return false
        assertFalse(testUser.hasOverdue());
    }

    // ===== canBorrow Tests =====

    @Test
    void testCanBorrow_NoIssues() {
        assertTrue(testUser.canBorrow());
    }

    @Test
    void testCanBorrow_WithUnpaidFines() {
        testUser.addFine(50.0);
        assertFalse(testUser.canBorrow());
    }

    // ===== canBeDeleted Tests =====

    @Test
    void testCanBeDeleted_NoFinesNoLoans() {
        assertTrue(testUser.canBeDeleted());
    }

    @Test
    void testCanBeDeleted_WithUnpaidFines() {
        testUser.addFine(50.0);
        assertFalse(testUser.canBeDeleted());
    }





    // ===== Delete Method Tests =====
    @Test
    void testDelete_User() {
        // Arrange
        clsUser user = clsUser.GetAddNewUserObject("deletetest_" + System.currentTimeMillis());
        user.setPassword("pass");
        user.save();

        // Act
        boolean deleted = user.delete();

        // Assert
        assertTrue(deleted);
        assertTrue(user.isEmpty()); // object reset
        assertFalse(clsUser.isUserExist(user.getUserName())); // removed from file
    }

    // ===== hasOverdue Tests with actual loans =====
    @Test
    void testHasOverdue_OverdueLoan() throws IOException {
        String username = "overdueuser_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("pass");
        user.save();

        // Create Loans.txt with one overdue book
        Path loans = Paths.get("Loans.txt");
        String line = "ISBN123#//#" + username + "#//#2025-11-01#//#2025-11-05#//#false"; // due in past
        Files.write(loans, line.getBytes());

        // Act
        boolean overdue = user.hasOverdue();

        // Assert
        assertTrue(overdue);

        // Cleanup
        Files.deleteIfExists(loans);
    }

    @Test
    void testHasOverdue_ReturnedLoan() throws IOException {
        String username = "returneduser_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("pass");
        user.save();

        // Create Loans.txt with returned loan
        Path loans = Paths.get("Loans.txt");
        String line = "ISBN123#//#" + username + "#//#2025-11-01#//#2025-11-05#//#true"; // already returned
        Files.write(loans, line.getBytes());

        // Act
        boolean overdue = user.hasOverdue();

        // Assert
        assertFalse(overdue);

        Files.deleteIfExists(loans);
    }

    // ===== canBeDeleted Tests with active loans =====
    @Test
    void testCanBeDeleted_WithActiveLoan() throws IOException {
        String username = "activeuser_" + System.currentTimeMillis();
        clsUser user = clsUser.GetAddNewUserObject(username);
        user.setPassword("pass");
        user.save();

        // Add a loan not returned
        Path loans = Paths.get("Loans.txt");
        String line = "ISBN123#//#" + username + "#//#2025-11-01#//#2025-12-01#//#false";
        Files.write(loans, line.getBytes());

        // Act
        boolean canDelete = user.canBeDeleted();

        // Assert
        assertFalse(canDelete);

        Files.deleteIfExists(loans);
    }

}