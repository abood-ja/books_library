package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TestClsAdminCDsMenuScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Enable test mode so _Show*Screen prints markers and _GoBack() does not block
        clsAdminCDsMenuScreen.TEST_MODE = true;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);

        clsInputValidate.setTestScanner(null);

        // Reset test mode
        clsAdminCDsMenuScreen.TEST_MODE = false;
    }

    @Test
    void test_ReadCDAdminMenuOption_ValidChoices() throws Exception {
        // Access the private method
        Method method = clsAdminCDsMenuScreen.class
                .getDeclaredMethod("_ReadCDAdminMenuOption");
        method.setAccessible(true);

        // Test all valid choices 1–5
        for (int i = 1; i <= 5; i++) {
            clsInputValidate.setTestScanner(new java.util.Scanner(String.valueOf(i)));

            Object result = method.invoke(null);

            // Compare directly to enum constants
            clsAdminCDsMenuScreen.enCDAdminMenuOptions expected =
                    clsAdminCDsMenuScreen.enCDAdminMenuOptions.values()[i - 1];

            assertEquals(expected, result);

            String output = outputStream.toString();
            assertTrue(output.contains("Choose what do you want to do? [1 to 5]?"));
            outputStream.reset();
        }
    }

    @Test
    void test_ReadCDAdminMenuOption_InvalidThenValid() throws Exception {
        // Access the private method
        Method method = clsAdminCDsMenuScreen.class
                .getDeclaredMethod("_ReadCDAdminMenuOption");
        method.setAccessible(true);

        // Simulate invalid input first, then valid input
        String simulatedInput = String.join(System.lineSeparator(),
                "0", // invalid
                "6", // invalid
                "3"  // valid
        );
        clsInputValidate.setTestScanner(new java.util.Scanner(simulatedInput));

        Object result = method.invoke(null);

        // Should return eFind (3)
        assertEquals(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eFind, result);

        String output = outputStream.toString();
        assertTrue(output.contains("Choose what do you want to do? [1 to 5]?"));
        assertTrue(output.contains("Enter number between 1 and 5!"));
    }

    @Test
    void test_GoBackPrintsMessage() throws Exception {
        // Provide simulated input so Scanner.nextLine() does not block
        System.setIn(new ByteArrayInputStream("\n".getBytes()));

        // Access the private _GoBack method via reflection
        Method method = clsAdminCDsMenuScreen.class.getDeclaredMethod("_GoBack");
        method.setAccessible(true);

        // Invoke the method (will immediately read the simulated Enter key)
        try {
            method.invoke(null);
        } catch (Exception ignored) {
            // In case showCDAdminMenu() causes more input issues, we ignore it for this test
        }

        // Check console output
        String output = outputStream.toString();
        assertTrue(output.contains("Press any key to go back..."));
    }


    private void invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions option) throws Exception {
        Method m = clsAdminCDsMenuScreen.class
                .getDeclaredMethod("_PerformOption", clsAdminCDsMenuScreen.enCDAdminMenuOptions.class);
        m.setAccessible(true);
        m.invoke(null, option);
    }

    @Test
    void test_PerformOption_List_PrintsMarker() throws Exception {
        invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eList);
        assertTrue(outputStream.toString().contains("list"));
    }

    @Test
    void test_PerformOption_Add_PrintsMarker() throws Exception {
        invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eAdd);
        assertTrue(outputStream.toString().contains("add"));
    }

    @Test
    void test_PerformOption_Find_PrintsMarker() throws Exception {
        invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eFind);
        assertTrue(outputStream.toString().contains("find"));
    }

    @Test
    void test_PerformOption_Delete_PrintsMarker() throws Exception {
        invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eDelete);
        assertTrue(outputStream.toString().contains("delete"));
    }

    @Test
    void test_PerformOption_Back_DoesNotPrint() throws Exception {
        invokePerformOption(clsAdminCDsMenuScreen.enCDAdminMenuOptions.eBack);
        // eBack should not print any marker
        String output = outputStream.toString();
        assertFalse(output.contains("list"));
        assertFalse(output.contains("add"));
        assertFalse(output.contains("find"));
        assertFalse(output.contains("delete"));
    }






}
