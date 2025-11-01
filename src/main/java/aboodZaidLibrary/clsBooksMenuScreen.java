package aboodZaidLibrary;

public class clsBooksMenuScreen extends clsScreen {
    private enum enBooksMenuOptions {
        eBorrowBook(1),
        eFindBook(2),
        eAddBook(3),
        eDeleteBook(4),
        eBackToMainMenu(5);

        private final int value;

        // Constructor
        enBooksMenuOptions(int value) {
            this.value = value;
        }

        // Getter
        public int getValue() {
            return value;
        }
    }
    private static enBooksMenuOptions _ReadBooksMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 5]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 5, "Enter Number between 1 to 5? ");

        // Convert int to enum
        for (enBooksMenuOptions o : enBooksMenuOptions.values()) {
            if (o.getValue() == choice) {
                return o;
            }
        }

        // Should never happen because readIntNumberBetween ensures 1-4
        return null;
    }
    private static  void _ShowBorrowBookScreen() {
        System.out.print("\nBorrow Book Screen Will be here...\n");
    }
    private static void _ShowFindBookScreen() {
        System.out.print("\nFind Book Screen Will be here...\n");
    }
    private static void _ShowAddBookScreen() {
        System.out.print("\nAdd Book Screen Will be here...\n");
    }
    private static void _ShowDeleteBookScreen() {
        System.out.print("\nDelete Book Screen Will be here...\n");
    }
    private static void _GoBackToBooksMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Books Menu...");
        new java.util.Scanner(System.in).nextLine();
        showBooksMenu();
    }
    private static void _PerformBooksMenuOption(enBooksMenuOptions option) {
        switch(option) {
            case eBorrowBook: // Example for your enum
                System.out.print("\033[H\033[2J"); // Clear console
                _ShowBorrowBookScreen();
                _GoBackToBooksMenu();
                break;

            case eFindBook:
                _ShowFindBookScreen();
                _GoBackToBooksMenu();
                break;

            case eAddBook:
                _ShowAddBookScreen();
                _GoBackToBooksMenu();
                break;

            case eDeleteBook:
                _ShowDeleteBookScreen();
                _GoBackToBooksMenu();
                break;

            case eBackToMainMenu:
                break;
        }
    }
    public static void showBooksMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tMain Screen");
        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tBooks Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Borrow Book .");
        System.out.println(pad + "\t[2] Find Book.");
        System.out.println(pad + "\t[3] Add Book.");
        System.out.println(pad + "\t[4] Delete Book.");
        System.out.println(pad + "\t[5] Main Menu.");
        System.out.println(pad + "===========================================");
        _PerformBooksMenuOption(_ReadBooksMenuOption());
    }
}
