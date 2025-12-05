package aboodZaidLibrary;

public class clsAdminCDsMenuScreen extends clsScreen {

    // -------------------------
    // TEST MODE FLAG
    // -------------------------
    public static boolean TEST_MODE = false;

    public enum enCDAdminMenuOptions {
        eList(1),
        eAdd(2),
        eFind(3),
        eDelete(4),
        eBack(5);

        private final int value;
        enCDAdminMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    // -------------------------
    // Show Screens (test-friendly)
    // -------------------------
    private static void _ShowCDsListScreen() {
        if (TEST_MODE) {
            System.out.print("list"); // marker for testing
        } else {
            clsCDsListScreen.showCDsListScreen();
        }
    }

    private static void _ShowAddNewCDScreen() {
        if (TEST_MODE) {
            System.out.print("add"); // marker for testing
        } else {
            clsAddNewCDScreen.showAddNewCDScreen();
        }
    }

    private static void _ShowFindCDScreen() {
        if (TEST_MODE) {
            System.out.print("find"); // marker for testing
        } else {
            clsFindCDScreen.showFindCDScreen();
        }
    }

    private static void _ShowDeleteCDScreen() {
        if (TEST_MODE) {
            System.out.print("delete"); // marker for testing
        } else {
            clsDeleteCDScreen.showDeleteCDScreen();
        }
    }

    // -------------------------
    // Read option
    // -------------------------
    private static enCDAdminMenuOptions _ReadCDAdminMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 5]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 5, "Enter number between 1 and 5!");

        for (enCDAdminMenuOptions m : enCDAdminMenuOptions.values())
            if (m.getValue() == choice)
                return m;

        return enCDAdminMenuOptions.eBack;
    }

    // -------------------------
    // Go back
    // -------------------------
    private static void _GoBack() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back...");
        new java.util.Scanner(System.in).nextLine();
        showCDAdminMenu();
    }

    // -------------------------
    // Perform option
    // -------------------------
    private static void _PerformOption(enCDAdminMenuOptions option) {
        switch (option) {

            case eList:
                System.out.print("\033[H\033[2J");
                _ShowCDsListScreen();
                if (!TEST_MODE) _GoBack();  // skip _GoBack in tests
                break;

            case eAdd:
                System.out.print("\033[H\033[2J");
                _ShowAddNewCDScreen();
                if (!TEST_MODE) _GoBack();
                break;

            case eFind:
                System.out.print("\033[H\033[2J");
                _ShowFindCDScreen();
                if (!TEST_MODE) _GoBack();
                break;

            case eDelete:
                System.out.print("\033[H\033[2J");
                _ShowDeleteCDScreen();
                if (!TEST_MODE) _GoBack();
                break;

            case eBack:
                break;
        }
    }

    // -------------------------
    // Show menu
    // -------------------------
    public static void showCDAdminMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        _DrawScreenHeader("\t\tCDs Admin Menu");

        String pad = String.format("%37s", "");

        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] CDs List");
        System.out.println(pad + "\t[2] Add New CD");
        System.out.println(pad + "\t[3] Find CD");
        System.out.println(pad + "\t[4] Delete CD");
        System.out.println(pad + "\t[5] Back");
        System.out.println(pad + "===========================================");

        _PerformOption(_ReadCDAdminMenuOption());
    }
}
