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
    private byte[] loansBackup; // Backup of Loans.txt

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));

        // Set a mock current user
        clsUserSession.currentUser = clsUser.GetAddNewUserObject("testuser");
        clsUserSession.currentUser.setFirstName("Test");
        clsUserSession.currentUser.setLastName("User");

        // Backup Loans.txt if it exists
        if (Files.exists(LOANS_FILE)) {
            loansBackup = Files.readAllBytes(LOANS_FILE);
        } else {
            loansBackup = null;
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);

        // Restore Loans.txt from backup
        if (loansBackup != null) {
            Files.write(LOANS_FILE, loansBackup, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } else if (Files.exists(LOANS_FILE)) {
            Files.delete(LOANS_FILE);
        }

        clsUserSession.currentUser = null;
        clsInputValidate.setTestScanner(null);
    }

    @Test
    void testNoLoansFile() throws IOException {
        // Ensure Loans.txt does not exist for this test
        Files.deleteIfExists(LOANS_FILE);

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("No loans found") || output.contains("You have no fines to pay"));
    }

    @Test
    void testNoFines() throws IOException {
        // Create empty Loans.txt safely
        Files.write(LOANS_FILE, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        clsPayFineScreen.showPayFineScreen();
        String output = outContent.toString();
        assertTrue(output.contains("You have no fines to pay"));
    }

    @Test
    void testBookFinePayment() throws IOException {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String loanLine = "ISBN123#//#testuser#//#Book Title#//#" + yesterday + "#//#false";

        // Write loan line safely
        Files.write(LOANS_FILE, loanLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

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
    void testBookFinePaymentY() throws IOException {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String loanLine = "ISBN123#//#testuser#//#Book Title#//#" + yesterday + "#//#false";

        // Write loan line safely
        Files.write(LOANS_FILE, loanLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Simulate user input 'y'
        clsInputValidate.setTestScanner(new java.util.Scanner("Y\n"));

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
        String loanLine = "CD-001#//#CD Title#//#testuser#//#other#//#" + yesterday + "#//#false";

        // Write loan line safely
        Files.write(LOANS_FILE, loanLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

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
        String loanLine = "ISBN123#//#testuser#//#Book Title#//#" + yesterday + "#//#false";

        // Write loan line safely
        Files.write(LOANS_FILE, loanLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

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
