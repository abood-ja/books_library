package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

class TestClsFine {

    private static final String LOANS_FILE = "Loans.txt";
    private static final String LOANS_BACKUP = "Loans_backup.txt";

    private static final String FINES_FILE = "Fines.txt";
    private static final String FINES_BACKUP = "Fines_backup.txt";

    @BeforeAll
    static void backupOriginalFiles() throws Exception {
        // Backup Loans.txt
        Path loansPath = Paths.get(LOANS_FILE);
        Path loansBackup = Paths.get(LOANS_BACKUP);
        if (Files.exists(loansPath)) {
            Files.copy(loansPath, loansBackup, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createFile(loansBackup);
        }

        // Backup Fines.txt
        Path finesPath = Paths.get(FINES_FILE);
        Path finesBackup = Paths.get(FINES_BACKUP);
        if (Files.exists(finesPath)) {
            Files.copy(finesPath, finesBackup, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.createFile(finesBackup);
        }
    }

    @BeforeEach
    void cleanFiles() throws Exception {
        // Clean up files for tests
        Files.deleteIfExists(Paths.get(LOANS_FILE));
        Files.deleteIfExists(Paths.get(FINES_FILE));
        Files.createFile(Paths.get(LOANS_FILE));
        Files.createFile(Paths.get(FINES_FILE));
    }

    @AfterAll
    static void restoreOriginalFiles() throws Exception {
        // Restore Loans.txt
        Path loansPath = Paths.get(LOANS_FILE);
        Path loansBackup = Paths.get(LOANS_BACKUP);
        Files.deleteIfExists(loansPath);
        if (Files.exists(loansBackup)) {
            Files.copy(loansBackup, loansPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(loansBackup);
        }

        // Restore Fines.txt
        Path finesPath = Paths.get(FINES_FILE);
        Path finesBackup = Paths.get(FINES_BACKUP);
        Files.deleteIfExists(finesPath);
        if (Files.exists(finesBackup)) {
            Files.copy(finesBackup, finesPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(finesBackup);
        }
    }

    // --- Your tests remain unchanged ---

    @Test
    void testGenerateFinesFromOverdueLoans_Book() throws Exception {
        LocalDate dueDate = LocalDate.now().minusDays(2);
        String loanLine = "BOOK-001#//#user1#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));

        clsFine.generateFinesFromOverdueLoans();

        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertEquals(1, fines.size());
        clsFine.FineRow fr = fines.get(0);
        assertEquals("user1", fr.username);
        assertEquals("BOOK-001", fr.mediaID);
        assertEquals("BOOK", fr.mediaType);
        assertEquals(2, fr.days);
        assertEquals(2, fr.amount);
    }

    @Test
    void testGenerateFinesFromOverdueLoans_CD() throws Exception {
        LocalDate dueDate = LocalDate.now().minusDays(3);
        String loanLine = "CD-001#//#user2#//#CD Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));

        clsFine.generateFinesFromOverdueLoans();

        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user2");
        assertEquals(1, fines.size());
        clsFine.FineRow fr = fines.get(0);
        assertEquals("user2", fr.username);
        assertEquals("CD-001", fr.mediaID);
        assertEquals("CD", fr.mediaType);
        assertEquals(3, fr.days);
        assertEquals(6, fr.amount);
    }

    @Test
    void testPayAllUserFines() throws Exception {
        LocalDate dueDate = LocalDate.now().minusDays(1);
        String loanLine = "BOOK-002#//#user3#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();

        clsFine.payAllUserFines("user3");

        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user3");
        assertEquals(0, fines.size());
        assertFalse(clsFine.hasUnpaidFines("user3"));
    }

    @Test
    void testGetTotalUserFines() throws Exception {
        LocalDate due1 = LocalDate.now().minusDays(2);
        LocalDate due2 = LocalDate.now().minusDays(3);
        String loan1 = "BOOK-003#//#user4#//#Book A#//#" + due1.toString() + "#//#false";
        String loan2 = "CD-002#//#user4#//#CD B#//#" + due2.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loan1, loan2));
        clsFine.generateFinesFromOverdueLoans();

        long total = clsFine.getTotalUserFines("user4");
        assertEquals(8, total); // 2 for book + 6 for CD
    }

    @Test
    void testHasUnpaidFines() throws Exception {
        LocalDate dueDate = LocalDate.now().minusDays(1);
        String loanLine = "BOOK-004#//#user5#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();

        assertTrue(clsFine.hasUnpaidFines("user5"));
        clsFine.payAllUserFines("user5");
        assertFalse(clsFine.hasUnpaidFines("user5"));
    }
}
