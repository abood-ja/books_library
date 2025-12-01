package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestClsInputValidate {

    @BeforeEach
    void setUp() {
        clsInputValidate.setTestScanner(null); // reset before each test
    }

    @AfterEach
    void tearDown() {
        clsInputValidate.setTestScanner(null); // reset after each test
    }

    @Test
    void testReadIntNumber_ValidInput() {
        clsInputValidate.setTestScanner(new Scanner("42\n"));
        int result = clsInputValidate.readIntNumber();
        assertEquals(42, result);
    }

    @Test
    void testReadIntNumber_InvalidThenValidInput() {
        clsInputValidate.setTestScanner(new Scanner("abc\n100\n"));
        int result = clsInputValidate.readIntNumber("Please enter a number:\n");
        assertEquals(100, result);
    }

    @Test
    void testReadIntNumberBetween_ValidInput() {
        clsInputValidate.setTestScanner(new Scanner("5\n"));
        int result = clsInputValidate.readIntNumberBetween(1, 10);
        assertEquals(5, result);
    }

    @Test
    void testReadIntNumberBetween_OutOfRangeThenValid() {
        clsInputValidate.setTestScanner(new Scanner("15\n7\n"));
        int result = clsInputValidate.readIntNumberBetween(1, 10);
        assertEquals(7, result);
    }

    @Test
    void testReadString() {
        clsInputValidate.setTestScanner(new Scanner("Hello World\n"));
        String result = clsInputValidate.readString();
        assertEquals("Hello World", result);
    }

    @Test
    void testReadChar_ValidChar() {
        clsInputValidate.setTestScanner(new Scanner("A\n"));
        char result = clsInputValidate.readChar();
        assertEquals('A', result);
    }

    @Test
    void testReadChar_EmptyInput() {
        clsInputValidate.setTestScanner(new Scanner("\n"));
        char result = clsInputValidate.readChar();
        assertEquals('\0', result);
    }

    @Test
    void testReadIntNumberBetween_WithErrorMessages() {
        clsInputValidate.setTestScanner(new Scanner("0\n11\n5\n"));
        int result = clsInputValidate.readIntNumberBetween(1, 10, "Out of range, enter again:\n");
        assertEquals(5, result);
    }
}
