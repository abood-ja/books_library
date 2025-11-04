package aboodZaidLibrary;

public class clsAdminMainScreen extends  clsScreen{
    private enum enMainMenuOptions {
        eBooks(1),
        eManageUsers(2),
        eExit(3);

        private final int value;

        // Constructor
        enMainMenuOptions(int value) {
            this.value = value;
        }

        // Getter
        public int getValue() {
            return value;
        }
    }
    private static clsAdminMainScreen.enMainMenuOptions _ReadMainMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 3]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 3, "Enter Number between 1 to 3? ");

        // Convert int to enum
        for (clsAdminMainScreen.enMainMenuOptions o : clsAdminMainScreen.enMainMenuOptions.values()) {
            if (o.getValue() == choice) {
                return o;
            }
        }

        // Should never happen because readIntNumberBetween ensures 1-4
        return null;
    }
    private static  void _ShowBooksMenu() {
        clsAdminBooksMenuScreen.showBooksMenu();
    }
    private static void _ShowManageUsersMenu() {
        clsManageUsersMenuScreen.showManageUsersMenu();
    }
    private static void _Logout() {
        clsUserSession.currentUser=clsUser.find("","");
    }
    private static void _GoBackToMainMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Main Menu...");
        new java.util.Scanner(System.in).nextLine();
        showMainMenu();
    }
    private static void _PerformMainMenuOption(enMainMenuOptions option) {
        switch(option) {
            case eBooks: // Example for your enum
                System.out.print("\033[H\033[2J"); // Clear console
                _ShowBooksMenu();
                _GoBackToMainMenu();
                break;
            case eManageUsers: // Example for your enum
                System.out.print("\033[H\033[2J"); // Clear console
                _ShowManageUsersMenu();
                _GoBackToMainMenu();
                break;

            case eExit:
                _Logout();
                break;
        }
    }
    public static void showMainMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tMain Screen");
        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tMain Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Books Menu.");
        System.out.println(pad + "\t[2] Manage Users Menu.");
        System.out.println(pad + "\t[3] Logout.");
        System.out.println(pad + "===========================================");
        _PerformMainMenuOption(_ReadMainMenuOption());
    }
}
