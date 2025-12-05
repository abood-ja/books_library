package aboodZaidLibrary;

public class clsManageUsersMenuScreen extends clsScreen{
    public static boolean TEST_MODE = false; // default false
    public enum enManageUsersMenuOptions {
        eUsersList(1),
        eAddNewUser(2),
        eFindUser(3),
        eDeleteUser(4),
        eLoginRegisterScreen(5),
        eBackToMainMenu(6);
        private final int value;

        // Constructor
        enManageUsersMenuOptions(int value) {
            this.value = value;
        }

        // Getter
        public int getValue() {
            return value;
        }
    }
    private static enManageUsersMenuOptions _ReadManageUsersMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 6]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 6, "Enter Number between 1 to 6? ");


        for (enManageUsersMenuOptions o : enManageUsersMenuOptions.values()) {
            if (o.getValue() == choice) {
                return o;
            }
        }


        return null;
    }
    private static void _ShowUsersListScreen() {
        if (TEST_MODE) {
            System.out.println("TEST_MODE: _ShowUsersListScreen called");
        } else {
            clsUserListScreen.showUserListScreen();
        }
    }

    private static void _ShowAddUsersListScreen() {
        if (TEST_MODE) {
            System.out.println("TEST_MODE: _ShowAddUsersListScreen called");
        } else {
            clsAddUserScreen.showAddUserScreen();
        }
    }

    private static void _ShowFindUsersListScreen() {
        if (TEST_MODE) {
            System.out.println("TEST_MODE: _ShowFindUsersListScreen called");
        } else {
            clsFindUserScreen.showFindUserScreen();
        }
    }

    private static void _ShowDeleteUsersListScreen() {
        if (TEST_MODE) {
            System.out.println("TEST_MODE: _ShowDeleteUsersListScreen called");
        } else {
            clsDeleteUserScreen.showDeleteUserScreen();
        }
    }

    private static void _ShowLoginRegisterScreen() {
        if (TEST_MODE) {
            System.out.println("TEST_MODE: _ShowLoginRegisterScreen called");
        } else {
            clsLoginRegisterScreen.showLoginRegisterScreen();
        }
    }
    private static void _GoBackToManageUsersMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Books Menu...");
        new java.util.Scanner(System.in).nextLine();
        showManageUsersMenu();
    }
    private static void _PerformManageUsersMenuOption(enManageUsersMenuOptions option) {
        switch(option) {
            case eUsersList:
                System.out.print("\033[H\033[2J");
                _ShowUsersListScreen();
                _GoBackToManageUsersMenu();
                break;
            case eAddNewUser: // Example for your enum
                System.out.print("\033[H\033[2J");
                _ShowAddUsersListScreen();
                _GoBackToManageUsersMenu();
                break;

            case eFindUser:
                System.out.print("\033[H\033[2J");
                _ShowFindUsersListScreen();
                _GoBackToManageUsersMenu();
                break;

            case eDeleteUser:
                System.out.print("\033[H\033[2J");
                _ShowDeleteUsersListScreen();
                _GoBackToManageUsersMenu();
                break;

            case eLoginRegisterScreen:
                System.out.print("\033[H\033[2J");
                _ShowLoginRegisterScreen();
                _GoBackToManageUsersMenu();
                break;

            case eBackToMainMenu:
                break;
        }
    }
    public static void showManageUsersMenu() {
        if(!clsScreen.CheckAccessRights(clsUser.enPermissions.pManageUsers)){
            return;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tMain Screen");
        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tManage Users Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Users List .");
        System.out.println(pad + "\t[2] Add New User .");
        System.out.println(pad + "\t[3] Find User.");
        System.out.println(pad + "\t[4] Delete User.");
        System.out.println(pad + "\t[5] Login Records Screen.");
        System.out.println(pad + "\t[6] Main Menu.");
        System.out.println(pad + "===========================================");
        _PerformManageUsersMenuOption(_ReadManageUsersMenuOption());
    }


}
