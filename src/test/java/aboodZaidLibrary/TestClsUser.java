package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
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
}