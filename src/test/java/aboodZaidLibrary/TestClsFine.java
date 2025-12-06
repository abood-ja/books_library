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
    @Test
    void testPayAllUserFines_WithNonEmptyLines() throws Exception {
        // Arrange - Create Fines.txt with actual fine data (non-empty lines)
        String fine1 = "user8#//#BOOK-007#//#BOOK#//#3#//#false";
        String fine2 = "user8#//#CD-003#//#CD#//#2#//#false";
        Files.write(Paths.get(FINES_FILE), Arrays.asList(fine1, fine2));

        // Act - Pay all fines for user8
        clsFine.payAllUserFines("user8");

        // Assert - All fines should be marked as paid (true)
        List<String> lines = Files.readAllLines(Paths.get(FINES_FILE));
        assertEquals(2, lines.size());

        for (String line : lines) {
            assertTrue(line.endsWith("#//#true")); // All should be marked paid
            assertTrue(line.contains("user8"));
        }

        // Verify through the API that no unpaid fines remain
        assertFalse(clsFine.hasUnpaidFines("user8"));
    }
    @Test
    void testGenerateFinesFromOverdueLoans_WithNonEmptyLines() throws Exception {
        // Arrange - Create Loans.txt with non-empty loan data
        LocalDate overdueDate = LocalDate.now().minusDays(5);
        String loan1 = "BOOK-100#//#user10#//#Some Book#//#" + overdueDate.toString() + "#//#false";
        String loan2 = "CD-100#//#user11#//#Some CD#//#" + overdueDate.toString() + "#//#false";

        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loan1, loan2));

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - Fines should be generated for both non-empty lines
        List<clsFine.FineRow> finesUser10 = clsFine.getUserUnpaidFines("user10");
        assertEquals(1, finesUser10.size());
        assertEquals("BOOK-100", finesUser10.get(0).mediaID);
        assertEquals(5, finesUser10.get(0).days);

        List<clsFine.FineRow> finesUser11 = clsFine.getUserUnpaidFines("user11");
        assertEquals(1, finesUser11.size());
        assertEquals("CD-100", finesUser11.get(0).mediaID);
        assertEquals(5, finesUser11.get(0).days);
    }

    @Test
    void testGenerateFinesFromOverdueLoans_MixedEmptyAndNonEmptyLines() throws Exception {
        // Arrange - Create Loans.txt with mix of empty and non-empty lines
        LocalDate overdueDate = LocalDate.now().minusDays(3);
        List<String> mixedLines = Arrays.asList(
                "",                                                                          // empty line
                "BOOK-101#//#user12#//#Book Title#//#" + overdueDate.toString() + "#//#false", // valid loan
                "   ",                                                                       // whitespace only
                "CD-101#//#user13#//#CD Title#//#" + overdueDate.toString() + "#//#false"      // valid loan
        );

        Files.write(Paths.get(LOANS_FILE), mixedLines);

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - Fines should only be generated for non-empty lines
        List<clsFine.FineRow> finesUser12 = clsFine.getUserUnpaidFines("user12");
        assertEquals(1, finesUser12.size());
        assertEquals("BOOK-101", finesUser12.get(0).mediaID);

        List<clsFine.FineRow> finesUser13 = clsFine.getUserUnpaidFines("user13");
        assertEquals(1, finesUser13.size());
        assertEquals("CD-101", finesUser13.get(0).mediaID);
    }
    @Test
    void testFineRow_DirectInstantiation() {
        // Arrange & Act - Create a FineRow object directly
        clsFine.FineRow fineRow = new clsFine.FineRow();

        // Set all fields
        fineRow.username = "testuser";
        fineRow.mediaID = "BOOK-999";
        fineRow.mediaType = "BOOK";
        fineRow.days = 5;
        fineRow.amount = 5;

        // Assert - Verify all fields are accessible and set correctly
        assertEquals("testuser", fineRow.username);
        assertEquals("BOOK-999", fineRow.mediaID);
        assertEquals("BOOK", fineRow.mediaType);
        assertEquals(5, fineRow.days);
        assertEquals(5, fineRow.amount);
    }
    @Test
    void testPayAllUserFines_WithMalformedLines() throws Exception {
        // Arrange - Create Fines.txt with mix of valid and malformed lines
        List<String> mixedLines = Arrays.asList(
                "user14#//#BOOK-009#//#BOOK#//#2#//#false",  // Valid (5 fields)
                "user14#//#BOOK-010#//#BOOK",                // Malformed (only 4 fields)
                "incomplete#//#data",                         // Malformed (only 3 fields)
                "user14#//#CD-005#//#CD#//#3#//#false"       // Valid (5 fields)
        );
        Files.write(Paths.get(FINES_FILE), mixedLines);

        // Act
        clsFine.payAllUserFines("user14");

        // Assert - Only valid lines (with >= 5 fields) should be in the result
        List<String> resultLines = Files.readAllLines(Paths.get(FINES_FILE));

        // Should have only 2 valid lines (malformed lines are skipped)
        assertEquals(2, resultLines.size());

        // All remaining lines should be marked as paid
        for (String line : resultLines) {
            String[] parts = line.split("#//#", -1);
            assertEquals(5, parts.length); // Only valid 5-field lines remain
            assertTrue(line.endsWith("#//#true")); // All marked paid
        }
    }

    @Test
    void testPayAllUserFines_OnlyMalformedLines() throws Exception {
        // Arrange - Create Fines.txt with only malformed lines
        List<String> malformedLines = Arrays.asList(
                "only#//#three#//#fields",        // 3 fields
                "four#//#fields#//#here#//#only", // 4 fields
                "two#//#fields"                   // 2 fields
        );
        Files.write(Paths.get(FINES_FILE), malformedLines);

        // Act
        clsFine.payAllUserFines("user15");

        // Assert - Result file should be empty (all malformed lines skipped)
        List<String> resultLines = Files.readAllLines(Paths.get(FINES_FILE));
        assertTrue(resultLines.isEmpty());
    }

    @Test
    void testPayAllUserFines_ValidAndMalformedForDifferentUsers() throws Exception {
        // Arrange - Mix of valid fines for different users and malformed lines
        List<String> mixedLines = Arrays.asList(
                "user16#//#BOOK-011#//#BOOK#//#1#//#false",  // Valid for user16
                "user17#//#CD-006",                          // Malformed for user17
                "user16#//#BOOK-012#//#BOOK#//#2#//#false",  // Valid for user16
                "incomplete"                                  // Malformed
        );
        Files.write(Paths.get(FINES_FILE), mixedLines);

        // Act - Pay fines for user16
        clsFine.payAllUserFines("user16");

        // Assert
        List<String> resultLines = Files.readAllLines(Paths.get(FINES_FILE));
        assertEquals(2, resultLines.size()); // Only 2 valid lines remain

        // Both should be for user16 and marked paid
        for (String line : resultLines) {
            assertTrue(line.startsWith("user16"));
            assertTrue(line.endsWith("#//#true"));
        }
    }
    @Test
    void testGenerateFinesFromOverdueLoans_ReturnedAndOverdue() throws Exception {
        // Arrange - Create a loan that is BOTH returned AND overdue
        // This tests the case where: returned=true AND today.isAfter(due)=true
        // The loan was returned late, but since it's marked as returned, no fine should be generated
        LocalDate overdueDate = LocalDate.now().minusDays(5);
        String loanLine = "BOOK-300#//#user30#//#Book Title#//#" + overdueDate.toString() + "#//#true";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - No fine should be generated (returned=true short-circuits the condition)
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user30");
        assertTrue(fines.isEmpty()); // No fines because item was returned
    }

    @Test
    void testGenerateFinesFromOverdueLoans_NotReturnedButNotOverdue() throws Exception {
        // Arrange - Create a loan that is NOT returned BUT also NOT overdue yet
        // This tests: returned=false AND today.isAfter(due)=false
        LocalDate futureDate = LocalDate.now().plusDays(5);
        String loanLine = "BOOK-301#//#user31#//#Book Title#//#" + futureDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - No fine should be generated (not overdue yet)
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user31");
        assertTrue(fines.isEmpty());
    }

    @Test
    void testGenerateFinesFromOverdueLoans_AllBranchCombinations() throws Exception {
        // Arrange - Create loans covering all 4 combinations
        LocalDate overdueDate = LocalDate.now().minusDays(3);
        LocalDate futureDate = LocalDate.now().plusDays(3);

        List<String> loans = Arrays.asList(
                // 1. NOT returned AND overdue → should generate fine
                "BOOK-400#//#user40#//#Book1#//#" + overdueDate.toString() + "#//#false",

                // 2. returned AND overdue → should NOT generate fine
                "BOOK-401#//#user41#//#Book2#//#" + overdueDate.toString() + "#//#true",

                // 3. NOT returned BUT NOT overdue → should NOT generate fine
                "BOOK-402#//#user42#//#Book3#//#" + futureDate.toString() + "#//#false",

                // 4. returned AND NOT overdue → should NOT generate fine
                "BOOK-403#//#user43#//#Book4#//#" + futureDate.toString() + "#//#true"
        );

        Files.write(Paths.get(LOANS_FILE), loans);

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - Only user40 should have a fine (case 1)
        assertTrue(clsFine.hasUnpaidFines("user40"));
        assertFalse(clsFine.hasUnpaidFines("user41")); // returned
        assertFalse(clsFine.hasUnpaidFines("user42")); // not overdue
        assertFalse(clsFine.hasUnpaidFines("user43")); // returned and not overdue

        // Verify the one fine that was generated
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user40");
        assertEquals(1, fines.size());
        assertEquals("BOOK-400", fines.get(0).mediaID);
    }
    @Test
    void testFineRow_AllFieldsAccessible() throws Exception {
        // Arrange - Create a fine in the file
        LocalDate overdueDate = LocalDate.now().minusDays(3);
        String loanLine = "BOOK-200#//#user20#//#Book#//#" + overdueDate.toString() + "#//#false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));

        // Act - Generate fines and get the FineRow object
        clsFine.generateFinesFromOverdueLoans();
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user20");

        // Assert - Access and verify ALL fields of FineRow
        assertEquals(1, fines.size());
        clsFine.FineRow fr = fines.get(0);

        assertNotNull(fr.username);
        assertNotNull(fr.mediaID);
        assertNotNull(fr.mediaType);
        assertTrue(fr.days > 0);
        assertTrue(fr.amount > 0);

        // Verify specific values
        assertEquals("user20", fr.username);
        assertEquals("BOOK-200", fr.mediaID);
        assertEquals("BOOK", fr.mediaType);
        assertEquals(3, fr.days);
        assertEquals(3, fr.amount); // 3 days * 1 NIS per day for books
    }
    @Test
    void testGenerateFinesFromOverdueLoans_OnlyEmptyLines() throws Exception {
        // Arrange - Create Loans.txt with only empty lines
        List<String> emptyLines = Arrays.asList("", "   ", "\t", "  \n  ");
        Files.write(Paths.get(LOANS_FILE), emptyLines);

        // Act
        clsFine.generateFinesFromOverdueLoans();

        // Assert - No fines should be generated
        List<String> finesContent = Files.readAllLines(Paths.get(FINES_FILE));
        assertTrue(finesContent.isEmpty() || finesContent.stream().allMatch(String::isEmpty));
    }
    @Test
    void testPayAllUserFines_MixedEmptyAndNonEmptyLines() throws Exception {
        // Arrange - Create Fines.txt with mix of empty and non-empty lines
        List<String> mixedLines = Arrays.asList(
                "user9#//#BOOK-008#//#BOOK#//#1#//#false",  // non-empty
                "",                                          // empty
                "   ",                                       // whitespace only
                "user9#//#CD-004#//#CD#//#2#//#false"       // non-empty
        );
        Files.write(Paths.get(FINES_FILE), mixedLines);

        // Act
        clsFine.payAllUserFines("user9");

        // Assert - Only non-empty lines should be in the result
        List<String> resultLines = Files.readAllLines(Paths.get(FINES_FILE));
        assertEquals(2, resultLines.size()); // Empty lines removed

        for (String line : resultLines) {
            assertFalse(line.trim().isEmpty()); // No empty lines
            assertTrue(line.endsWith("#//#true")); // All marked paid
        }
    }
    // --- Your tests remain unchanged ---

    @Test
    void testEnsureFinesFile_FileAlreadyExists() throws Exception {
        // Arrange - Make sure Fines.txt exists with some content
        Path finesPath = Paths.get(FINES_FILE);
        String existingContent = "user1#//#BOOK-001#//#BOOK#//#2#//#false";
        Files.write(finesPath, Arrays.asList(existingContent));

        // Verify file exists before the call
        assertTrue(Files.exists(finesPath));

        // Act - Call a method that invokes ensureFinesFile() when file already exists
        // This will test the false branch of "if (!Files.exists(p))"
        clsFine.generateFinesFromOverdueLoans();

        // Assert - File should still exist and content should be preserved
        assertTrue(Files.exists(finesPath));
        List<String> lines = Files.readAllLines(finesPath);
        assertTrue(lines.contains(existingContent)); // Original content preserved
    }

    @Test
    void testEnsureFinesFile_FileDoesNotExist() throws Exception {
        // Arrange - Delete Fines.txt to ensure it doesn't exist
        Path finesPath = Paths.get(FINES_FILE);
        Files.deleteIfExists(finesPath);

        // Verify file doesn't exist
        assertFalse(Files.exists(finesPath));

        // Act - Call a method that invokes ensureFinesFile() when file doesn't exist
        // This will test the true branch of "if (!Files.exists(p))"
        clsFine.generateFinesFromOverdueLoans();

        // Assert - File should now exist
        assertTrue(Files.exists(finesPath));
    }

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


    // NEW TESTS FOR 90%+ BRANCH COVERAGE

    @Test
    void testGetUserUnpaidFinesNoFile() throws Exception {
        Files.deleteIfExists(Paths.get(FINES_FILE));
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertTrue(fines.isEmpty()); // Covers !Files.exists(p) branch [file:1]
    }

    @Test
    void testGetUserUnpaidFinesReturnedFine() throws Exception {
        String returnedFine = "user1|BOOK-001|BOOK|2|true";
        Files.write(Paths.get(FINES_FILE), Arrays.asList(returnedFine));
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertTrue(fines.isEmpty()); // Covers returned.equals("false") false branch [file:1]
    }

    @Test
    void testGetUserUnpaidFinesMalformedLine() throws Exception {
        String malformed = "only4fields|no|sep|here";
        Files.write(Paths.get(FINES_FILE), Arrays.asList(malformed));
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertTrue(fines.isEmpty()); // Covers f.length < 5 skip [file:1]
    }

    @Test
    void testGetUserUnpaidFinesWrongUser() throws Exception {
        String otherUserFine = "otheruser|BOOK-999|BOOK|5|false";
        Files.write(Paths.get(FINES_FILE), Arrays.asList(otherUserFine));
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertTrue(fines.isEmpty()); // Covers u.equals(username) false [file:1]
    }

    @Test
    void testGetUserUnpaidFinesEmptyLine() throws Exception {
        Files.write(Paths.get(FINES_FILE), Arrays.asList("   ", ""));
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user1");
        assertTrue(fines.isEmpty()); // Covers line.trim().isEmpty() [file:1]
    }

    @Test
    void testGenerateFinesFromOverdueLoansNoLoansFile() throws Exception {
        Files.deleteIfExists(Paths.get(LOANS_FILE));
        clsFine.generateFinesFromOverdueLoans();
        assertTrue(Files.readAllLines(Paths.get(FINES_FILE)).isEmpty()); // Covers !Files.exists(p) [file:1]
    }

    @Test
    void testGenerateFinesFromOverdueLoansNotOverdue() throws Exception {
        LocalDate futureDue = LocalDate.now().plusDays(1);
        String loanLine = "BOOK-005|user6|Book Title|" + futureDue.toString() + "|false";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user6");
        assertTrue(fines.isEmpty()); // Covers today.isAfter(due) false [file:1]
    }

    @Test
    void testGenerateFinesFromOverdueLoansAlreadyReturned() throws Exception {
        LocalDate dueDate = LocalDate.now().minusDays(2);
        String loanLine = "BOOK-006|user7|Book Title|" + dueDate.toString() + "|true";
        Files.write(Paths.get(LOANS_FILE), Arrays.asList(loanLine));
        clsFine.generateFinesFromOverdueLoans();
        List<clsFine.FineRow> fines = clsFine.getUserUnpaidFines("user7");
        assertTrue(fines.isEmpty()); // Covers !returned false branch [file:1]
    }





    @Test
    void testPayAllUserFinesNoFines() throws Exception {
        clsFine.payAllUserFines("nonexistent");
        // File unchanged, covers for-loop skip branches [file:1]
    }

    @Test
    void testPayAllUserFinesEmptyFinesFile() throws Exception {
        Files.write(Paths.get(FINES_FILE), new ArrayList<>()); // Empty file
        clsFine.payAllUserFines("user10");
        // Covers empty lines loop skip [file:1]
    }


}
