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
    }

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
    }

    @Test
    void testDrawScreenHeader_WithoutSubTitle() {
        clsUser user = clsUser.GetAddNewUserObject("admin");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPermissions(-1);
        clsUserSession.currentUser = user;

        clsScreen._DrawScreenHeader("Main Screen");
        String output = outContent.toString();

        assertTrue(output.contains("Main Screen"));
        // SubTitle should not appear
        assertFalse(output.contains("null"));
        assertTrue(output.contains("User: Admin User"));
    }
}
