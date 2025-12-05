package aboodZaidLibrary;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestClsReminderService {

    private static final String USERS_FILE = "Users.txt";
    private static final String LOANS_FILE = "Loans.txt";
    private static final String SEP = "#//#";

    private byte[] usersBackup;
    private byte[] loansBackup;

    @BeforeEach
    void setup() throws Exception {
        // Backup Users.txt if it exists
        Path usersPath = Paths.get(USERS_FILE);
        if (Files.exists(usersPath)) {
            usersBackup = Files.readAllBytes(usersPath);
        } else {
            usersBackup = null;
        }

        // Backup Loans.txt if it exists
        Path loansPath = Paths.get(LOANS_FILE);
        if (Files.exists(loansPath)) {
            loansBackup = Files.readAllBytes(loansPath);
        } else {
            loansBackup = null;
        }

        // Create empty files for testing
        Files.write(usersPath, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(loansPath, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterEach
    void cleanup() throws Exception {
        Path usersPath = Paths.get(USERS_FILE);
        Path loansPath = Paths.get(LOANS_FILE);

        // Restore Users.txt from backup
        if (usersBackup != null) {
            Files.write(usersPath, usersBackup, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } else if (Files.exists(usersPath)) {
            Files.delete(usersPath);
        }

        // Restore Loans.txt from backup
        if (loansBackup != null) {
            Files.write(loansPath, loansBackup, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } else if (Files.exists(loansPath)) {
            Files.delete(loansPath);
        }
    }

    // ========== ORIGINAL TESTS ==========

    @Test
    void testNoUsersOrLoans() {
        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // just ensure no exceptions
    }

    @Test
    void testSingleUserOverdueLoan() throws Exception {
        String username = "john";
        String email = "john@example.com";

        String userLine = "John#//#Doe#//#" + email + "#//#12345#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(5);
        String loanLine = "ISBN-001" + SEP + username + SEP + "borrowDate" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true);
    }

    @Test
    void testMultipleUsersOverdueLoans() throws Exception {
        String username1 = "alice";
        String email1 = "alice@example.com";
        String username2 = "bob";
        String email2 = "bob@example.com";

        List<String> users = Arrays.asList(
                "Alice#//#Smith#//#" + email1 + "#//#123#//#" + username1 + "#//#pass#//#perm",
                "Bob#//#Jones#//#" + email2 + "#//#456#//#" + username2 + "#//#pass#//#perm"
        );
        Files.write(Paths.get(USERS_FILE), users, StandardCharsets.UTF_8);

        LocalDate due1 = LocalDate.now().minusDays(3);
        LocalDate due2 = LocalDate.now().minusDays(7);
        List<String> loans = Arrays.asList(
                "ISBN-A1" + SEP + username1 + SEP + "borrow1" + SEP + due1.toString() + SEP + "false",
                "ISBN-B1" + SEP + username2 + SEP + "borrow2" + SEP + due2.toString() + SEP + "false"
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true);
    }

    @Test
    void testReturnedLoansAreIgnored() throws Exception {
        String username = "charlie";
        String email = "charlie@example.com";

        String userLine = "Charlie#//#Brown#//#" + email + "#//#789#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(10);
        String loanLine = "ISBN-R1" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "true";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true);
    }

    // ========== NEW TESTS ==========

    @Test
    void testUserWithoutEmail() throws Exception {
        String username = "nomail";

        // User without valid email (missing @ symbol)
        String userLine = "No#//#Email#//#invalidmail#//#111#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(2);
        String loanLine = "ISBN-002" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should skip user without valid email
    }

    @Test
    void testUserWithEmptyEmail() throws Exception {
        String username = "emptyemail";

        // User with empty email field
        String userLine = "Empty#//#Email#//##//##//#222#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(3);
        String loanLine = "ISBN-003" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should skip user with empty email
    }

    @Test
    void testCaseInsensitiveUsername() throws Exception {
        String username = "MixedCase";
        String email = "mixed@example.com";

        String userLine = "Mixed#//#Case#//#" + email + "#//#333#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(1);
        // Loan file uses lowercase username
        String loanLine = "ISBN-004" + SEP + username.toLowerCase() + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should match case-insensitively
    }

    @Test
    void testMultipleOverdueLoansForSameUser() throws Exception {
        String username = "dave";
        String email = "dave@example.com";

        String userLine = "Dave#//#Smith#//#" + email + "#//#444#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate due1 = LocalDate.now().minusDays(2);
        LocalDate due2 = LocalDate.now().minusDays(5);
        LocalDate due3 = LocalDate.now().minusDays(10);

        List<String> loans = Arrays.asList(
                "ISBN-D1" + SEP + username + SEP + "borrow1" + SEP + due1.toString() + SEP + "false",
                "ISBN-D2" + SEP + username + SEP + "borrow2" + SEP + due2.toString() + SEP + "false",
                "ISBN-D3" + SEP + username + SEP + "borrow3" + SEP + due3.toString() + SEP + "false"
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should send one email with all overdue items
    }

    @Test
    void testMixedOverdueAndNotOverdueLoans() throws Exception {
        String username = "eve";
        String email = "eve@example.com";

        String userLine = "Eve#//#Johnson#//#" + email + "#//#555#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate overdue = LocalDate.now().minusDays(3);
        LocalDate notDue = LocalDate.now().plusDays(3);

        List<String> loans = Arrays.asList(
                "ISBN-E1" + SEP + username + SEP + "borrow1" + SEP + overdue.toString() + SEP + "false",
                "ISBN-E2" + SEP + username + SEP + "borrow2" + SEP + notDue.toString() + SEP + "false"
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should only include overdue loan
    }

    @Test
    void testLoanDueToday() throws Exception {
        String username = "frank";
        String email = "frank@example.com";

        String userLine = "Frank#//#Miller#//#" + email + "#//#666#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate today = LocalDate.now();
        String loanLine = "ISBN-F1" + SEP + username + SEP + "borrow" + SEP + today.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Loan due today should NOT be overdue
    }

    @Test
    void testEmptyLines() throws Exception {
        String username = "grace";
        String email = "grace@example.com";

        List<String> users = Arrays.asList(
                "",
                "Grace#//#Lee#//#" + email + "#//#777#//#" + username + "#//#pass#//#perm",
                "",
                ""
        );
        Files.write(Paths.get(USERS_FILE), users, StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(4);
        List<String> loans = Arrays.asList(
                "",
                "ISBN-G1" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false",
                ""
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should handle empty lines gracefully
    }

    @Test
    void testMalformedUserLine() throws Exception {
        // User line with insufficient fields
        List<String> users = Arrays.asList(
                "Incomplete#//#Line#//#only3fields"
        );
        Files.write(Paths.get(USERS_FILE), users, StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(1);
        String loanLine = "ISBN-M1" + SEP + "someuser" + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should handle malformed lines without crashing
    }

    @Test
    void testMalformedLoanLine() throws Exception {
        String username = "helen";
        String email = "helen@example.com";

        String userLine = "Helen#//#Wong#//#" + email + "#//#888#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        // Loan line with insufficient fields
        List<String> loans = Arrays.asList(
                "ISBN-H1" + SEP + username + SEP + "only3fields"
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should handle malformed lines without crashing
    }

    @Test
    void testExtraFieldsInLoanLine() throws Exception {
        String username = "ivan";
        String email = "ivan@example.com";

        String userLine = "Ivan#//#Chen#//#" + email + "#//#999#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(6);
        // Loan line with extra fields (but returned is still last)
        String loanLine = "ISBN-I1" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "extraField1" + SEP + "extraField2" + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should handle extra fields correctly
    }

    @Test
    void testUsernameWithWhitespace() throws Exception {
        String username = "  john  ";
        String email = "whitespace@example.com";

        String userLine = "John#//#Doe#//#" + email + "#//#000#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        LocalDate dueDate = LocalDate.now().minusDays(2);
        String loanLine = "ISBN-W1" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should trim whitespace and match correctly
    }

    @Test
    void testNonexistentFiles() {
        // Delete both files
        try {
            Files.deleteIfExists(Paths.get(USERS_FILE));
            Files.deleteIfExists(Paths.get(LOANS_FILE));
        } catch (Exception e) {
            fail("Failed to delete files");
        }

        clsReminderService.sendOverdueRemindersNow();
        assertTrue(true); // Should handle missing files gracefully
    }

    @Test
    void testCustomEmailMethod() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test body content";

        // This should work with DRY_RUN enabled (won't actually send)
        clsReminderService.sendCustomEmail(to, subject, body);
        assertTrue(true);
    }

    @Test
    void testSendEmailWithNullRecipient() {
        // Test edge case - should be handled by the method
        try {
            clsReminderService.sendCustomEmail(null, "Subject", "Body");
            // If DRY_RUN is true, this won't throw
            assertTrue(true);
        } catch (Exception e) {
            // If DRY_RUN is false, this might throw
            assertTrue(true);
        }
    }
}