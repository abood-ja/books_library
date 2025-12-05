package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TestClsManageUsersMenuScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        clsManageUsersMenuScreen.TEST_MODE = true; // enable test mode for all tests
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        clsManageUsersMenuScreen.TEST_MODE = false; // reset after tests
    }


    private clsManageUsersMenuScreen.enManageUsersMenuOptions invokeReadManageUsersMenuOption(String simulatedInput) throws Exception {
        // Simulate user input
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Access private static method via reflection
        Method method = clsManageUsersMenuScreen.class.getDeclaredMethod("_ReadManageUsersMenuOption");
        method.setAccessible(true);

        // Invoke the method and cast the result
        return (clsManageUsersMenuScreen.enManageUsersMenuOptions) method.invoke(null);
    }

    @Test
    void testOption1_UsersList() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("1\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eUsersList, result);
    }

    @Test
    void testOption2_AddNewUser() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("2\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eAddNewUser, result);
    }

    @Test
    void testOption3_FindUser() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("3\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eFindUser, result);
    }

    @Test
    void testOption4_DeleteUser() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("4\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eDeleteUser, result);
    }

    @Test
    void testOption5_LoginRegisterScreen() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("5\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eLoginRegisterScreen, result);
    }

    @Test
    void testOption6_BackToMainMenu() throws Exception {
        clsManageUsersMenuScreen.enManageUsersMenuOptions result = invokeReadManageUsersMenuOption("6\n");
        assertEquals(clsManageUsersMenuScreen.enManageUsersMenuOptions.eBackToMainMenu, result);
    }

    private void invokePrivateMethod(String methodName) throws Exception {
        Method method = clsManageUsersMenuScreen.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(null);
    }
    @Test
    void test_ShowUsersListScreen() throws Exception {
        invokePrivateMethod("_ShowUsersListScreen");
        assertTrue(outputStream.toString().contains("_ShowUsersListScreen called"));
    }

    @Test
    void test_ShowAddUsersListScreen() throws Exception {
        invokePrivateMethod("_ShowAddUsersListScreen");
        assertTrue(outputStream.toString().contains("_ShowAddUsersListScreen called"));
    }

    @Test
    void test_ShowFindUsersListScreen() throws Exception {
        invokePrivateMethod("_ShowFindUsersListScreen");
        assertTrue(outputStream.toString().contains("_ShowFindUsersListScreen called"));
    }

    @Test
    void test_ShowDeleteUsersListScreen() throws Exception {
        invokePrivateMethod("_ShowDeleteUsersListScreen");
        assertTrue(outputStream.toString().contains("_ShowDeleteUsersListScreen called"));
    }

    @Test
    void test_ShowLoginRegisterScreen() throws Exception {
        invokePrivateMethod("_ShowLoginRegisterScreen");
        assertTrue(outputStream.toString().contains("_ShowLoginRegisterScreen called"));
    }

    @Test
    void test_GoBackToManageUsersMenu_printPrompt() throws Exception {
        // Simulate user pressing Enter
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        // Call private method via reflection
        Method method = clsManageUsersMenuScreen.class.getDeclaredMethod("_GoBackToManageUsersMenu");
        method.setAccessible(true);

        // Invoke the method (it will call showManageUsersMenu(), we can't avoid that)
        // We just want to verify the prompt is printed
        try {
            method.invoke(null);
        } catch (Exception ignored) {
            // If showManageUsersMenu throws or tries to read input, ignore for now
        }

        // Check if the "Press any key..." prompt was printed
        String output = outputStream.toString();
        assertTrue(output.contains("Press any key to go back to Books Menu..."));
    }


}
