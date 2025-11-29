package aboodZaidLibrary;

public class clsAdminCDsMenuScreen extends clsScreen {

    private enum enCDAdminMenuOptions {
        eList(1),
        eAdd(2),
        eFind(3),
        eDelete(4),
        eBack(5);

        private final int value;
        enCDAdminMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enCDAdminMenuOptions _ReadCDAdminMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 5]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 5, "Enter number between 1 and 5!");

        for (enCDAdminMenuOptions m : enCDAdminMenuOptions.values())
            if (m.getValue() == choice)
                return m;

        return enCDAdminMenuOptions.eBack;
    }

    private static void _GoBack() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back...");
        new java.util.Scanner(System.in).nextLine();
        showCDAdminMenu();
    }

    private static void _PerformOption(enCDAdminMenuOptions option) {
        switch (option) {

            case eList:
                System.out.print("\033[H\033[2J");
                clsCDsListScreen.showCDsListScreen();
                _GoBack();
                break;

            case eAdd:
                System.out.print("\033[H\033[2J");
                clsAddNewCDScreen.showAddNewCDScreen();
                _GoBack();
                break;

            case eFind:
                System.out.print("\033[H\033[2J");
                clsFindCDScreen.showFindCDScreen();
                _GoBack();
                break;

            case eDelete:
                System.out.print("\033[H\033[2J");
                clsDeleteCDScreen.showDeleteCDScreen();
                _GoBack();
                break;

            case eBack:
                break;
        }
    }

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
