package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;

class TestClsScreen {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        clsUserSession.currentUser = clsUser.find("", ""); // reset currentUser if needed
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    // ============== Tests for CheckAccessRights ==============

    @Test
    void testCheckAccessRights_Allowed() {
        // Mock currentUser with full access
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setPermissions(-1); // full access
        clsUserSession.currentUser = user;

        boolean access = clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook);
        assertTrue(access);

        // Should not print "Access Denied!"
        String output = outContent.toString();
        assertFalse(output.contains("Access Denied"));
    }

    @Test
    void testCheckAccessRights_Denied() {
        // Mock currentUser with no permissions
        clsUser user = clsUser.GetAddNewUserObject("limited");
        user.setPermissions(0); // no access
        clsUserSession.currentUser = user;

        boolean access = clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook);
        assertFalse(access);

        // Should print "Access Denied!"
        String output = outContent.toString();
        assertTrue(output.contains("Access Denied!"));
        assertTrue(output.contains("Contact your Admin"));
    }

    @Test
    void testCheckAccessRights_AllPermissions() {
        // Test with different permission types to ensure branch works for all
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setPermissions(-1); // full access
        clsUserSession.currentUser = user;

        // Test multiple permissions
        assertTrue(clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook));
        assertTrue(clsScreen.CheckAccessRights(clsUser.enPermissions.eAll));
    }

    @Test
    void testCheckAccessRights_NoPermissions() {
        // Test denial with different permission types
        clsUser user = clsUser.GetAddNewUserObject("limited");
        user.setPermissions(0); // no access
        clsUserSession.currentUser = user;

        // All should be denied
        assertFalse(clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook));

        String output = outContent.toString();
        assertTrue(output.contains("Access Denied!"));
    }

    // ============== Tests for _DrawScreenHeader with SubTitle ==============

    @Test
    void testDrawScreenHeader_WithSubTitle() {
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPermissions(-1);
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Main Screen", "Sub Screen");
        String output = outContent.toString();

        assertTrue(output.contains("Main Screen"));
        assertTrue(output.contains("Sub Screen"));
        assertTrue(output.contains("User: Admin User"));
        assertTrue(output.contains("Date:"));
        assertTrue(output.contains("______________________________________"));
    }

    @Test
    void testDrawScreenHeader_WithSubTitle_NonEmptySubTitle() {
        // Tests the branch where subTitle != null && !subTitle.isEmpty()
        clsUser user = clsUser.GetAddNewUserObject("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Test Title", "Test Subtitle");
        String output = outContent.toString();

        assertTrue(output.contains("Test Title"));
        assertTrue(output.contains("Test Subtitle"));
    }

    @Test
    void testDrawScreenHeader_WithNullSubTitle() {
        // Tests the branch where subTitle == null
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Admin");
        user.setLastName("User");
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Main Screen", null);
        String output = outContent.toString();

        assertTrue(output.contains("Main Screen"));
        // SubTitle line should not be printed
        int titleCount = output.split("Main Screen").length - 1;
        assertEquals(1, titleCount); // Only one occurrence of title
    }


    // ============== Tests for _DrawScreenHeader without SubTitle ==============

    @Test
    void testDrawScreenHeader_WithoutSubTitle() {
        // This calls the overloaded method that passes "" as subTitle
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPermissions(-1);
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Main Screen");
        String output = outContent.toString();

        assertTrue(output.contains("Main Screen"));
        assertTrue(output.contains("User: Admin User"));
        assertTrue(output.contains("Date:"));
    }

    // ============== Tests for currentUser.isEmpty() branch ==============

    @Test
    void testDrawScreenHeader_WithEmptyCurrentUser() {
        // Tests the branch where currentUser.isEmpty() == true
        clsUser emptyUser = clsUser.find("", ""); // Assuming this returns an empty user
        clsUserSession.currentUser = emptyUser;

        clsScreen._DrawScreenHeader("Test Screen", "Test Subtitle");
        String output = outContent.toString();

        assertTrue(output.contains("Test Screen"));
        // Should not print "User:" line when currentUser is empty
        if (emptyUser.isEmpty()) {
            assertFalse(output.contains("User:"));
        }
    }

    @Test
    void testDrawScreenHeader_WithNonEmptyCurrentUser() {
        // Tests the branch where currentUser.isEmpty() == false
        clsUser user = clsUser.GetAddNewUserObject("validuser");
        user.setFirstName("Valid");
        user.setLastName("User");
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Test Screen");
        String output = outContent.toString();

        assertTrue(output.contains("User: Valid User"));
    }

    // ============== Edge Cases and Integration Tests ==============

    @Test
    void testDrawScreenHeader_VerifyFormatting() {
        // Verify the padding and formatting is consistent
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("John");
        user.setLastName("Doe");
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Title", "Subtitle");
        String output = outContent.toString();

        // Check that underscores appear (formatting check)
        assertTrue(output.contains("______________________________________"));
        assertTrue(output.contains("Title"));
        assertTrue(output.contains("Subtitle"));
        assertTrue(output.contains("User: John Doe"));
        assertTrue(output.contains("Date:"));
    }

    @Test
    void testDrawScreenHeader_LongTitles() {
        // Test with longer strings
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Administrator");
        user.setLastName("SuperUser");
        clsUserSession.currentUser = user;

        String longTitle = "This Is A Very Long Title For Testing";
        String longSubtitle = "This Is Also A Long Subtitle";

        clsScreen._DrawScreenHeader(longTitle, longSubtitle);
        String output = outContent.toString();

        assertTrue(output.contains(longTitle));
        assertTrue(output.contains(longSubtitle));
    }

    @Test
    void testCheckAccessRights_VerifyOutputFormat() {
        // Verify the exact format of access denied message
        clsUser user = clsUser.GetAddNewUserObject("limited");
        user.setPermissions(0);
        clsUserSession.currentUser = user;

        clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook);
        String output = outContent.toString();

        // Check all parts of the message
        assertTrue(output.contains("______________________________________"));
        assertTrue(output.contains("Access Denied!"));
        assertTrue(output.contains("Contact your Admin"));
    }

    @Test
    void testDrawScreenHeader_DateIsPresent() {
        // Ensure date is always printed
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Test");
        user.setLastName("User");
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Test", "");
        String output = outContent.toString();

        assertTrue(output.contains("Date:"));
        // Verify date format (should contain /)
        assertTrue(output.matches("(?s).*Date: \\d+/\\d+/\\d+.*"));
    }

    @Test
    void testCheckAccessRights_TrueAndFalseBranches() {
        // Explicitly test both true and false branches
        clsUser adminUser = clsUser.GetAddNewUserObject("admin");
        adminUser.setPermissions(-1);

        clsUser limitedUser = clsUser.GetAddNewUserObject("limited");
        limitedUser.setPermissions(0);

        // Test true branch (else)
        clsUserSession.currentUser = adminUser;
        boolean hasAccess = clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook);
        assertTrue(hasAccess);
        String output1 = outContent.toString();
        assertFalse(output1.contains("Access Denied"));

        // Reset output
        outContent.reset();

        // Test false branch (if)
        clsUserSession.currentUser = limitedUser;
        boolean noAccess = clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook);
        assertFalse(noAccess);
        String output2 = outContent.toString();
        assertTrue(output2.contains("Access Denied"));
    }

    @Test
    void testDrawScreenHeader_SubTitleBothConditions() {
        clsUser user = clsUser.GetAddNewUserObject("test");
        user.setFirstName("Test");
        user.setLastName("User");
        clsUserSession.currentUser = user;

        // Test with non-null, non-empty subtitle (true branch)
        clsScreen._DrawScreenHeader("Title1", "Subtitle1");
        String output1 = outContent.toString();
        assertTrue(output1.contains("Subtitle1"));

        // Reset
        outContent.reset();

        // Test with null subtitle (false branch - null check)
        clsScreen._DrawScreenHeader("Title2", null);
        String output2 = outContent.toString();
        assertFalse(output2.contains("null"));

        // Reset
        outContent.reset();

        // Test with empty subtitle (false branch - isEmpty check)
        clsScreen._DrawScreenHeader("Title3", "");
        String output3 = outContent.toString();
        // Should not have extra subtitle line
        assertTrue(output3.contains("Title3"));
    }

    @Test
    void testDrawScreenHeader_CurrentUserBothBranches() {
        // Test with non-empty user (false branch of isEmpty())
        clsUser validUser = clsUser.GetAddNewUserObject("valid");
        validUser.setFirstName("Valid");
        validUser.setLastName("User");
        clsUserSession.currentUser = validUser;

        clsScreen._DrawScreenHeader("Test1");
        String output1 = outContent.toString();

        if (!validUser.isEmpty()) {
            assertTrue(output1.contains("User: Valid User"));
        }

        // Reset
        outContent.reset();

        // Test with empty user (true branch of isEmpty())
        clsUser emptyUser = clsUser.find("", "");
        clsUserSession.currentUser = emptyUser;

        clsScreen._DrawScreenHeader("Test2");
        String output2 = outContent.toString();

        if (emptyUser.isEmpty()) {
            assertFalse(output2.contains("User:"));
        }
    }
}