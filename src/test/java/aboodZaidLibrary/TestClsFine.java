package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

class TestClsFine {

    @BeforeEach
    void cleanFiles() throws Exception {
        // Clean up Loans.txt and Fines.txt before each test
        Files.deleteIfExists(Paths.get("Loans.txt"));
        Files.deleteIfExists(Paths.get("Fines.txt"));
    }

    @Test
    void testGenerateFinesFromOverdueLoans_Book() throws Exception {
        // Arrange: create a loan for a book overdue by 2 days
        LocalDate dueDate = LocalDate.now().minusDays(2);
        String loanLine = "BOOK-001#//#user1#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get("Loans.txt"), Arrays.asList(loanLine));

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertEquals(1, fines.size());
        clsFine.FineRow fr = fines.get(0);
        assertEquals("user1", fr.username);
        assertEquals("BOOK-001", fr.mediaID);
        assertEquals("BOOK", fr.mediaType);
        assertEquals(2, fr.days);
        assertEquals(2, fr.amount); // 1 per day
    }

    @Test
    void testGenerateFinesFromOverdueLoans_CD() throws Exception {
        // Arrange: create a loan for a CD overdue by 3 days
        LocalDate dueDate = LocalDate.now().minusDays(3);
        String loanLine = "CD-001#//#user2#//#CD Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get("Loans.txt"), Arrays.asList(loanLine));

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user2");
        assertEquals(1, fines.size());
        clsFine.FineRow fr = fines.get(0);
        assertEquals("user2", fr.username);
        assertEquals("CD-001", fr.mediaID);
        assertEquals("CD", fr.mediaType);
        assertEquals(3, fr.days);
        assertEquals(6, fr.amount); // 2 per day
    }

    @Test
    void testPayAllUserFines() throws Exception {
        // Arrange
        LocalDate dueDate = LocalDate.now().minusDays(1);
        String loanLine = "BOOK-002#//#user3#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get("Loans.txt"), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();

        // Act
        clsFine.payAllUserFines("user3");

        // Assert
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user3");
        assertEquals(0, fines.size());
        assertFalse(clsFine.hasUnpaidFines("user3"));
    }

    @Test
    void testGetTotalUserFines() throws Exception {
        // Arrange: 2 fines
        LocalDate due1 = LocalDate.now().minusDays(2);
        LocalDate due2 = LocalDate.now().minusDays(3);
        String loan1 = "BOOK-003#//#user4#//#Book A#//#" + due1.toString() + "#//#false";
        String loan2 = "CD-002#//#user4#//#CD B#//#" + due2.toString() + "#//#false";
        Files.write(Paths.get("Loans.txt"), Arrays.asList(loan1, loan2));
        clsFine.generateFinesFromOverdueLoans();

        // Act
        long total = clsFine.getTotalUserFines("user4");

        // Assert
        // Book fine: 2*1=2, CD fine: 3*2=6, total=8
        assertEquals(8, total);
    }

    @Test
    void testHasUnpaidFines() throws Exception {
        // Arrange
        LocalDate dueDate = LocalDate.now().minusDays(1);
        String loanLine = "BOOK-004#//#user5#//#Book Title#//#" + dueDate.toString() + "#//#false";
        Files.write(Paths.get("Loans.txt"), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();

        // Act & Assert
        assertTrue(clsFine.hasUnpaidFines("user5"));
        clsFine.payAllUserFines("user5");
        assertFalse(clsFine.hasUnpaidFines("user5"));
    }
}
