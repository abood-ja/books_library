package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class TestClsUserMainScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws Exception {
        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Set a dummy current user
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
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setOut(originalOut);
        System.setIn(originalIn);
        clsUserSession.currentUser = null;
    }

    private String invokePrivateMethod(String methodName) throws Exception {
        Method method = clsUserMainScreen.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(null); // static method → null instance
        return outputStream.toString().trim();
    }

    @Test
    void testShowMainMenuPrintsExpectedOutput() throws Exception {
        System.setIn(new ByteArrayInputStream("4\n".getBytes())); // simulate "Logout"
        clsUserMainScreen.showMainMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("User Main Menu"));
        assertTrue(output.contains("[1] Books Menu."));
        assertTrue(output.contains("[2] CDs Menu."));
        assertTrue(output.contains("[3] Pay Fines."));
        assertTrue(output.contains("[4] Logout."));
        assertTrue(output.contains("==========================================="));
    }







    @Test
    void test_ReadMainMenuOption_validInput1() throws Exception {
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        Method method = clsUserMainScreen.class.getDeclaredMethod("_ReadMainMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);
        assertEquals(clsUserMainScreen.enMainMenuOptions.eBooksMenu, result);
    }

    @Test
    void test_ReadMainMenuOption_validInput4() throws Exception {
        System.setIn(new ByteArrayInputStream("4\n".getBytes()));
        Method method = clsUserMainScreen.class.getDeclaredMethod("_ReadMainMenuOption");
        method.setAccessible(true);
        Object result = method.invoke(null);
        assertEquals(clsUserMainScreen.enMainMenuOptions.eLogout, result);
    }

    @Test
    void test_Logout_resetsCurrentUser() throws Exception {
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

        Method logoutMethod = clsUserMainScreen.class.getDeclaredMethod("_Logout");
        logoutMethod.setAccessible(true);
        logoutMethod.invoke(null);

        clsUser expectedUser = clsUser.find("", "");
        assertEquals(expectedUser.getUserName(), clsUserSession.currentUser.getUserName());
        assertEquals(expectedUser.getFirstName(), clsUserSession.currentUser.getFirstName());
        assertEquals(expectedUser.getLastName(), clsUserSession.currentUser.getLastName());
        assertEquals(expectedUser.getEmail(), clsUserSession.currentUser.getEmail());
        assertEquals(expectedUser.getPhone(), clsUserSession.currentUser.getPhone());
    }
}
