package aboodZaidLibrary;

public class clsUserMainScreen extends clsScreen {
    private static boolean TEST_MODE = false; // set true in tests
    public enum enMainMenuOptions {
        eBooksMenu(1),
        eCDsMenu(2),
        ePayFines(3),
        eLogout(4);

        private final int value;
        enMainMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enMainMenuOptions _ReadMainMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose [1-4]: ");

        int choice = clsInputValidate.readIntNumberBetween(1, 4, "Enter number between 1 and 4: ");

        for (enMainMenuOptions o : enMainMenuOptions.values())
            if (o.getValue() == choice) return o;

        return enMainMenuOptions.eLogout;
    }

    private static void _ShowBooksMenu() {
        if (TEST_MODE) {
            System.out.println("[TEST_MODE] _ShowBooksMenu called");
        } else {
            clsUserBooksMenuScreen.showBooksMenu();
        }
    }

    private static void _ShowCDsMenu() {
        if (TEST_MODE) {
            System.out.println("[TEST_MODE] _ShowCDsMenu called");
        } else {
            clsUserCDsMenuScreen.showCDsMenu();
        }
    }

    private static void _ShowPayFinesScreen() {
        if (TEST_MODE) {
            System.out.println("[TEST_MODE] _ShowPayFinesScreen called");
        } else {
            clsPayFineScreen.showPayFineScreen();
        }
    }
    private static void _Logout() {
        if (TEST_MODE) {
            // In test mode, just set to null for easy testing
            clsUserSession.currentUser = null;
        } else {
            // Normal behavior
            clsUserSession.currentUser = clsUser.find("", "");
        }
    }
    private static void _PerformMainMenuOption(enMainMenuOptions option) {
        switch(option) {
            case eBooksMenu:
                System.out.print("\033[H\033[2J");
                _ShowBooksMenu();
                showMainMenu();
                break;

            case eCDsMenu:
                System.out.print("\033[H\033[2J");
                _ShowCDsMenu();
                showMainMenu();
                break;

            case ePayFines:
                System.out.print("\033[H\033[2J");
                _ShowPayFinesScreen();
                showMainMenu();
                break;

            case eLogout:
                _Logout();
                break;
        }
    }

    public static void showMainMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tUser Main Menu");

        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tUser Main Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Books Menu.");
        System.out.println(pad + "\t[2] CDs Menu.");
        System.out.println(pad + "\t[3] Pay Fines.");   // <-- فقط هنا
        System.out.println(pad + "\t[4] Logout.");
        System.out.println(pad + "===========================================");

        _PerformMainMenuOption(_ReadMainMenuOption());
    }
}