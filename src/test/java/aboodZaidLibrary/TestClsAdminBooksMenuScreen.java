package aboodZaidLibrary;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TestClsAdminBooksMenuScreen {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUpStreams() {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreSystemIO() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * Helper: simulate user input
     */
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    /**
     * Helper: invoke private _ReadBooksMenuOption() method
     */
    private Object invokeReadBooksMenuOption() throws Exception {
        Method method = clsAdminBooksMenuScreen.class
                .getDeclaredMethod("_ReadBooksMenuOption");
        method.setAccessible(true);
        return method.invoke(null);
    }

    // -------------------------
    // Test _ReadBooksMenuOption()
    // -------------------------
    @Test
    void test_ReadBooksMenuOption_ReturnsBooksList() throws Exception {
        provideInput("1\n");
        Object result = invokeReadBooksMenuOption();
        assertEquals(clsAdminBooksMenuScreen.enBooksMenuOptions.eBooksList, result);
    }

    @Test
    void test_ReadBooksMenuOption_ReturnsFindBook() throws Exception {
        provideInput("2\n");
        Object result = invokeReadBooksMenuOption();
        assertEquals(clsAdminBooksMenuScreen.enBooksMenuOptions.eFindBook, result);
    }

    @Test
    void test_ReadBooksMenuOption_ReturnsAddBook() throws Exception {
        provideInput("3\n");
        Object result = invokeReadBooksMenuOption();
        assertEquals(clsAdminBooksMenuScreen.enBooksMenuOptions.eAddBook, result);
    }

    @Test
    void test_ReadBooksMenuOption_ReturnsDeleteBook() throws Exception {
        provideInput("4\n");
        Object result = invokeReadBooksMenuOption();
        assertEquals(clsAdminBooksMenuScreen.enBooksMenuOptions.eDeleteBook, result);
    }

    @Test
    void test_ReadBooksMenuOption_ReturnsBackToMainMenu() throws Exception {
        provideInput("5\n");
        Object result = invokeReadBooksMenuOption();
        assertEquals(clsAdminBooksMenuScreen.enBooksMenuOptions.eBackToMainMenu, result);
    }

    // -------------------------
    // Test _Show*Screen() methods
    // -------------------------
    @Test
    void test_ShowBooksListScreen_NoExceptions() throws Exception {
        Method m = clsAdminBooksMenuScreen.class
                .getDeclaredMethod("_ShowBooksListScreen");
        m.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }



    @Test
    void test_ShowAddBookScreen_NoExceptions() throws Exception {
        Method m = clsAdminBooksMenuScreen.class
                .getDeclaredMethod("_ShowAddBookScreen");
        m.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void test_ShowDeleteBookScreen_NoExceptions() throws Exception {
        Method m = clsAdminBooksMenuScreen.class
                .getDeclaredMethod("_ShowDeleteBookScreen");
        m.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // -------------------------
    // Test _GoBackToBooksMenu()
    // -------------------------
    @Test
    void test_GoBackToBooksMenu_PrintsMessage() throws Exception {
        // Simulate pressing Enter so Scanner.nextLine() does not block
        provideInput("\n");

        // Access the private _GoBackToBooksMenu method via reflection
        Method method = clsAdminBooksMenuScreen.class
                .getDeclaredMethod("_GoBackToBooksMenu");
        method.setAccessible(true);

        // Invoke the method (ignore exceptions from showBooksMenu())
        try {
            method.invoke(null);
        } catch (Exception ignored) {}

        // Check console output contains the expected message
        String output = outputStream.toString();
        assertTrue(output.contains("Press any key to go back to Books Menu..."));
    }

    @Test
    void test_showBooksMenu_PrintsMenu() throws Exception {
        // Simulate pressing '5' (Main Menu) so _ReadBooksMenuOption() returns safely
        System.setIn(new ByteArrayInputStream("5\n".getBytes()));

        // Access the showBooksMenu method via reflection
        Method method = clsAdminBooksMenuScreen.class.getDeclaredMethod("showBooksMenu");
        method.setAccessible(true);

        // Invoke the method (ignore exceptions from _PerformBooksMenuOption)
        try {
            method.invoke(null);
        } catch (Exception ignored) {}

        // Verify that the printed menu matches the Books Menu text
        String output = outputStream.toString();
        assertTrue(output.contains("Main Screen"));
        assertTrue(output.contains("Books Menu"));
        assertTrue(output.contains("[1] Books List ."));
        assertTrue(output.contains("[2] Find Book."));
        assertTrue(output.contains("[3] Add Book."));
        assertTrue(output.contains("[4] Delete Book."));
        assertTrue(output.contains("[5] Main Menu."));
    }
}
