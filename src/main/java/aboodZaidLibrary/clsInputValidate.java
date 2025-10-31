package aboodZaidLibrary;
import java.util.Scanner;

public class clsInputValidate {
	public static int readIntNumber(String errorMessage) {
    Scanner scanner = new Scanner(System.in);
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
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }
}
