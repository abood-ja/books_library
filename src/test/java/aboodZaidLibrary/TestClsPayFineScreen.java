package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;

import org.junit.jupiter.api.*;

class TestClsPayFineScreen {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private static final Path LOANS_FILE = Paths.get("Loans.txt");

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));

        // Set a mock current user
        clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
        clsUserSession.currentUser.setFirstName("Test");
        clsUserSession.currentUser.setLastName("User");

        // Delete loans file to start fresh
        Files.deleteIfExists(LOANS_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        Files.deleteIfExists(LOANS_FILE);
    }

    @Test
    void testNoLoansFile() {
        // Loans file does not exist
        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("No loans found") || output.contains("You have no fines to pay"));
    }

    @Test
    void testNoFines() throws IOException {
        // Create empty loans file
        Files.createFile(LOANS_FILE);

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("You have no fines to pay"));
    }

    @Test
    void testBookFinePayment() throws IOException {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String loanLine = "ISBN123" + "#//#" + "testuser" + "#//#" + "Book Title" + "#//#" + yesterday + "#//#" + "false";
        Files.write(LOANS_FILE, loanLine.getBytes());

        // Simulate user input 'y'
        clsInputValidate.setTestScanner(new java.util.Scanner("y\n"));

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("Unpaid Fines"));
        assertTrue(output.contains("✔ All fines paid successfully"));

        // Check that the loan line was updated to 'true'
        String updatedLine = Files.readAllLines(LOANS_FILE).get(0);
        assertTrue(updatedLine.endsWith("true"));
    }

    @Test
    void testCDFinePayment() throws IOException {
        LocalDate yesterday = LocalDate.now().minusDays(2);
        // CD format: 6 parts
        String loanLine = "CD-001" + "#//#" + "CD Title" + "#//#" + "testuser" + "#//#" + "other" + "#//#" + yesterday + "#//#" + "false";
        Files.write(LOANS_FILE, loanLine.getBytes());

        // Simulate user input 'y'
        clsInputValidate.setTestScanner(new java.util.Scanner("y\n"));

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("Unpaid Fines"));
        assertTrue(output.contains("✔ All fines paid successfully"));

        // Check that the loan line was updated to 'true'
        String updatedLine = Files.readAllLines(LOANS_FILE).get(0);
        assertTrue(updatedLine.endsWith("true"));
    }

    @Test
    void testPaymentCanceled() throws IOException {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String loanLine = "ISBN123" + "#//#" + "testuser" + "#//#" + "Book Title" + "#//#" + yesterday + "#//#" + "false";
        Files.write(LOANS_FILE, loanLine.getBytes());

        // Simulate user input 'n'
        clsInputValidate.setTestScanner(new java.util.Scanner("n\n"));

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("Payment canceled"));

        // Ensure loan line was NOT changed
        String unchangedLine = Files.readAllLines(LOANS_FILE).get(0);
        assertTrue(unchangedLine.endsWith("false"));
    }
}
