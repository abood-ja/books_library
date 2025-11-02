package aboodZaidLibrary;

public class clsBooksMenuScreen extends clsScreen {
    private enum enBooksMenuOptions {
        eBooksList(1),
        eBorrowBook(2),
        eFindBook(3),
        eAddBook(4),
        eDeleteBook(5),
        eBackToMainMenu(6);

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
        System.out.print(pad + "Choose what do you want to do? [1 to 6]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 6, "Enter Number between 1 to 6? ");

        // Convert int to enum
        for (enBooksMenuOptions o : enBooksMenuOptions.values()) {
            if (o.getValue() == choice) {
                return o;
            }
        }

        // Should never happen because readIntNumberBetween ensures 1-4
        return null;
    }
    private  static void _ShowBooksListScreen(){
        clsBooksListScreen.showBooksListScreen();
    }
    private static  void _ShowBorrowBookScreen() {
        System.out.print("\nBorrow Book Screen Will be here...\n");
    }
    private static void _ShowFindBookScreen() {
        System.out.print("\nFind Book Screen Will be here...\n");
    }
    private static void _ShowAddBookScreen() {
        clsAddNewBookScreen.showAddNewBookScreen();
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
            case eBooksList:
                System.out.print("\033[H\033[2J");
                _ShowBooksListScreen();
                _GoBackToBooksMenu();
                break;
            case eBorrowBook: // Example for your enum
                System.out.print("\033[H\033[2J");
                _ShowBorrowBookScreen();
                _GoBackToBooksMenu();
                break;

            case eFindBook:
                 System.out.print("\033[H\033[2J");
                _ShowFindBookScreen();
                _GoBackToBooksMenu();
                break;

            case eAddBook:
                System.out.print("\033[H\033[2J");
                _ShowAddBookScreen();
                _GoBackToBooksMenu();
                break;

            case eDeleteBook:
                System.out.print("\033[H\033[2J");
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
        System.out.println(pad + "\t[1] Books List .");
        System.out.println(pad + "\t[2] Borrow Book .");
        System.out.println(pad + "\t[3] Find Book.");
        System.out.println(pad + "\t[4] Add Book.");
        System.out.println(pad + "\t[5] Delete Book.");
        System.out.println(pad + "\t[6] Main Menu.");
        System.out.println(pad + "===========================================");
        _PerformBooksMenuOption(_ReadBooksMenuOption());
    }
}
