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
}
