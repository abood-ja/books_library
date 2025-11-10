package aboodZaidLibrary;

import java.nio.file.*;
import java.util.*;

public class clsAdminMainScreen extends clsScreen {

    private enum enMainMenuOptions {
        eBooks(1),
        eManageUsers(2),
        eSendReminders(3),
        eViewAllLoans(4),
        eExit(5);

        private final int value;
        enMainMenuOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static enMainMenuOptions _ReadMainMenuOption() {
        String pad = String.format("%37s", "");
        System.out.print(pad + "Choose what do you want to do? [1 to 5]? ");
        int choice = clsInputValidate.readIntNumberBetween(1, 5, "Enter Number between 1 to 5? ");
        for (enMainMenuOptions o : enMainMenuOptions.values())
            if (o.getValue() == choice) return o;
        return enMainMenuOptions.eExit; // safety
    }

    private static void _ShowBooksMenu() {
        clsAdminBooksMenuScreen.showBooksMenu();
    }

    private static void _ShowManageUsersMenu() {
        clsManageUsersMenuScreen.showManageUsersMenu();
    }

    private static void _SendOverdueReminders() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tSend Overdue Reminders");

        // ملاحظة: أثناء الاختبار خلّي DRY_RUN = true داخل clsReminderService
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
                    "Username", "ISBN", "Borrow", "Due", "Returned");
            System.out.println("--------------------------------------------------------------");

            for (String line : lines) {
                String[] parts = line.split("#//#");
                if (parts.length >= 5) {
                    String isbn = parts[0];
                    String username = parts[1];
                    String borrowDate = parts[2];
                    String dueDate = parts[3];
                    String returned = parts[4];
                    System.out.printf("%-15s %-15s %-15s %-15s %-10s%n",
                            username, isbn, borrowDate, dueDate, returned);
                }
            }
        } catch (Exception e) {
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

    private static void _PerformMainMenuOption(enMainMenuOptions option) {
        switch (option) {
            case eBooks:
                System.out.print("\033[H\033[2J");
                _ShowBooksMenu();
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
        System.out.println(pad + "\t[3] Send Overdue Reminders.");
        System.out.println(pad + "\t[4] View All Loans.");
        System.out.println(pad + "\t[5] Logout.");
        System.out.println(pad + "===========================================");

        _PerformMainMenuOption(_ReadMainMenuOption());
    }
}
