package aboodZaidLibrary;

import java.nio.file.*;
import java.util.*;

public class clsAdminMainScreen extends clsScreen {

    private enum enMainMenuOptions {
        eBooks(1),
        eCDs(2),
        eManageUsers(3),
        eSendReminders(4),
        eViewAllLoans(5),
        eLogout(6);

        private final int value;
        enMainMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enMainMenuOptions _ReadMainMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 6]? ");

        int choice = clsInputValidate.readIntNumberBetween(1, 6, "Enter Number between 1 to 6? ");

        for (enMainMenuOptions o : enMainMenuOptions.values())
            if (o.getValue() == choice)
                return o;

        return enMainMenuOptions.eLogout;
    }

    // ======================= SUB-MENUS =========================

    private static void _ShowBooksMenu() {
        clsAdminBooksMenuScreen.showBooksMenu();
    }

    private static void _ShowCDsMenu() {
        clsAdminCDsMenuScreen.showCDAdminMenu();
    }

    private static void _ShowManageUsersMenu() {
        clsManageUsersMenuScreen.showManageUsersMenu();
    }

    private static void _SendOverdueReminders() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tSend Overdue Reminders");

        clsReminderService.sendOverdueRemindersNow();
    }

    private static void _ShowAllLoans() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tAll Active Loans");

        Path path = Paths.get("Loans.txt");

        if (!Files.exists(path)) {
            System.out.println("\nNo loans found.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                System.out.println("\nNo loans found.");
                return;
            }

            System.out.printf("%-15s %-15s %-15s %-15s %-10s%n",
                    "Username", "ISBN/CDID", "Borrow", "Due", "Returned");

            System.out.println("-----------------------------------------------------------------");

            for (String line : lines) {
                String[] p = line.split("#//#");
                if (p.length >= 5) {
                    String isbn = p[0];
                    String username = p[1];
                    String borrowDate = p[2];
                    String dueDate = p[3];
                    String returned = p[4];

                    System.out.printf("%-15s %-15s %-15s %-15s %-10s%n",
                            username, isbn, borrowDate, dueDate, returned);
                }
            }
        }
        catch (Exception e) {
            System.out.println("\nError reading loans: " + e.getMessage());
        }
    }

    private static void _Logout() {
        clsUserSession.currentUser = clsUser.find("", "");
    }

    private static void _GoBackToMainMenu() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "\nPress any key to go back to Main Menu...");
        new java.util.Scanner(System.in).nextLine();
        showMainMenu();
    }

    // ======================= PERFORM OPTION =========================

    private static void _PerformMainMenuOption(enMainMenuOptions option) {

        switch (option) {

            case eBooks:
                System.out.print("\033[H\033[2J");
                _ShowBooksMenu();
                _GoBackToMainMenu();
                break;

            case eCDs:
                System.out.print("\033[H\033[2J");
                _ShowCDsMenu();
                _GoBackToMainMenu();
                break;

            case eManageUsers:
                System.out.print("\033[H\033[2J");
                _ShowManageUsersMenu();
                _GoBackToMainMenu();
                break;

            case eSendReminders:
                _SendOverdueReminders();
                _GoBackToMainMenu();
                break;

            case eViewAllLoans:
                _ShowAllLoans();
                _GoBackToMainMenu();
                break;

            case eLogout:
                _Logout();
                break;
        }
    }

    // ======================= MAIN MENU UI =========================

    public static void showMainMenu() {

        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tAdmin Main Menu");

        String pad = String.format("%37s", "");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t\t\tMain Menu");
        System.out.println(pad + "===========================================");
        System.out.println(pad + "\t[1] Books Menu.");
        System.out.println(pad + "\t[2] CDs Menu.");
        System.out.println(pad + "\t[3] Manage Users Menu.");
        System.out.println(pad + "\t[4] Send Overdue Reminders.");
        System.out.println(pad + "\t[5] View All Loans.");
        System.out.println(pad + "\t[6] Logout.");
        System.out.println(pad + "===========================================");

        _PerformMainMenuOption(_ReadMainMenuOption());
    }
}
