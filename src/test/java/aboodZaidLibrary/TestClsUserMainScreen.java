package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TestClsUserMainScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws Exception {
        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Set TEST_MODE = true before each test
        Field testModeField = clsUserMainScreen.class.getDeclaredField("TEST_MODE");
        testModeField.setAccessible(true);
        testModeField.setBoolean(null, true); // static field → null instance
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
    void tearDown() {
        System.setOut(originalOut);
        clsUserSession.currentUser = null; // clean up
    }


    private String invokePrivateMethod(String methodName) throws Exception {
        Method method = clsUserMainScreen.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(null); // static method → null instance
        return outputStream.toString().trim();
    }

    @Test
    void test_ShowBooksMenu() throws Exception {
        String output = invokePrivateMethod("_ShowBooksMenu");
        assertEquals("[TEST_MODE] _ShowBooksMenu called", output);
    }

    @Test
    void test_ShowCDsMenu() throws Exception {
        String output = invokePrivateMethod("_ShowCDsMenu");
        assertEquals("[TEST_MODE] _ShowCDsMenu called", output);
    }

    @Test
    void test_ShowPayFinesScreen() throws Exception {
        String output = invokePrivateMethod("_ShowPayFinesScreen");
        assertEquals("[TEST_MODE] _ShowPayFinesScreen called", output);
    }
    @Test
    void test_ReadMainMenuOption_validInput1() throws Exception {
        // Simulate user typing "1" and pressing Enter
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Access private static method via reflection
        Method method = clsUserMainScreen.class.getDeclaredMethod("_ReadMainMenuOption");
        method.setAccessible(true);

        Object result = method.invoke(null); // static method → null instance
        assertEquals(clsUserMainScreen.enMainMenuOptions.eBooksMenu, result);
    }

    @Test
    void test_ReadMainMenuOption_validInput4() throws Exception {
        // Simulate user typing "4" and pressing Enter
        String simulatedInput = "4\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Method method = clsUserMainScreen.class.getDeclaredMethod("_ReadMainMenuOption");
        method.setAccessible(true);

        Object result = method.invoke(null);
        assertEquals(clsUserMainScreen.enMainMenuOptions.eLogout, result);
    }


    @Test
    void test_Logout_resetsCurrentUser() throws Exception {
        // Ensure currentUser is not null before logout
        assertNotNull(clsUserSession.currentUser);

        // Call private static _Logout()
        Method logoutMethod = clsUserMainScreen.class.getDeclaredMethod("_Logout");
        logoutMethod.setAccessible(true);
        logoutMethod.invoke(null); // static method → null instance

        // Check that currentUser is now null
        assertNull(clsUserSession.currentUser, "currentUser should be null after _Logout()");
    }





}
