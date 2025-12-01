package aboodZaidLibrary;

import java.util.Scanner;

public class clsInputValidate {

    private static Scanner testScanner = null; // Scanner for testing

    // Set a test Scanner (used in unit tests)
    public static void setTestScanner(Scanner sc) {
        testScanner = sc;
    }

    // Get the current Scanner (test or real System.in)
    private static Scanner getScanner() {
        return (testScanner != null) ? testScanner : new Scanner(System.in);
    }

    public static int readIntNumber(String errorMessage) {
        Scanner scanner = getScanner();
        int number;

        while (true) {
            try {
                number = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print(errorMessage);
            }
        }

        return number;
    }

    public static int readIntNumber() {
        return readIntNumber("Invalid Number, Enter again\n");
    }

    public static int readIntNumberBetween(int from, int to, String errorMessage) {
        int number = readIntNumber();

        while (number < from || number > to) {
            System.out.print(errorMessage);
            number = readIntNumber();
        }

        return number;
    }

    public static int readIntNumberBetween(int from, int to) {
        return readIntNumberBetween(from, to, "Number is not within range, Enter again:\n");
    }

    public static String readString() {
        Scanner scanner = getScanner();
        return scanner.nextLine();
    }

    public static char readChar() {
        String input = readString();
        if (input.isEmpty()) return '\0';
        return input.charAt(0);
    }
}
