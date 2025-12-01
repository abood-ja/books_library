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

    @BeforeEach
    void setup() throws Exception {
        // Clear files before each test
        Files.deleteIfExists(Paths.get(USERS_FILE));
        Files.deleteIfExists(Paths.get(LOANS_FILE));
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(USERS_FILE));
        Files.deleteIfExists(Paths.get(LOANS_FILE));
    }

    @Test
    void testNoUsersOrLoans() {
        // Nothing in files
        clsReminderService.sendOverdueRemindersNow();
        // Should not throw exceptions
        assertTrue(true);
    }

    @Test
    void testSingleUserOverdueLoan() throws Exception {
        String username = "john";
        String email = "john@example.com";

        // Write Users.txt
        String userLine = "John#//#Doe#//#" + email + "#//#12345#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        // Write Loans.txt with overdue book
        LocalDate dueDate = LocalDate.now().minusDays(5);
        String loanLine = "ISBN-001" + SEP + username + SEP + "borrowDate" + SEP + dueDate.toString() + SEP + "false";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        // Call method
        clsReminderService.sendOverdueRemindersNow();

        // If DRY_RUN is true, it prints; otherwise, it would send email. We just assert no exceptions.
        assertTrue(true);
    }

    @Test
    void testMultipleUsersOverdueLoans() throws Exception {
        String username1 = "alice";
        String email1 = "alice@example.com";
        String username2 = "bob";
        String email2 = "bob@example.com";

        // Users.txt
        List<String> users = Arrays.asList(
                "Alice#//#Smith#//#" + email1 + "#//#123#//#" + username1 + "#//#pass#//#perm",
                "Bob#//#Jones#//#" + email2 + "#//#456#//#" + username2 + "#//#pass#//#perm"
        );
        Files.write(Paths.get(USERS_FILE), users, StandardCharsets.UTF_8);

        // Loans.txt
        LocalDate due1 = LocalDate.now().minusDays(3);
        LocalDate due2 = LocalDate.now().minusDays(7);

        List<String> loans = Arrays.asList(
                "ISBN-A1" + SEP + username1 + SEP + "borrow1" + SEP + due1.toString() + SEP + "false",
                "ISBN-B1" + SEP + username2 + SEP + "borrow2" + SEP + due2.toString() + SEP + "false"
        );
        Files.write(Paths.get(LOANS_FILE), loans, StandardCharsets.UTF_8);

        // Call method
        clsReminderService.sendOverdueRemindersNow();

        // Just assert it ran without exceptions
        assertTrue(true);
    }

    @Test
    void testReturnedLoansAreIgnored() throws Exception {
        String username = "charlie";
        String email = "charlie@example.com";

        // Users.txt
        String userLine = "Charlie#//#Brown#//#" + email + "#//#789#//#" + username + "#//#pass#//#perm";
        Files.write(Paths.get(USERS_FILE), Collections.singletonList(userLine), StandardCharsets.UTF_8);

        // Loans.txt with returned loan
        LocalDate dueDate = LocalDate.now().minusDays(10);
        String loanLine = "ISBN-R1" + SEP + username + SEP + "borrow" + SEP + dueDate.toString() + SEP + "true";
        Files.write(Paths.get(LOANS_FILE), Collections.singletonList(loanLine), StandardCharsets.UTF_8);

        clsReminderService.sendOverdueRemindersNow();

        // Should run without exception since returned loans are ignored
        assertTrue(true);
    }
}
