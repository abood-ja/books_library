package aboodZaidLibrary;
import java.util.Vector;

public class clsLoginRegisterScreen extends clsScreen{
    protected static void showLoginRegisterScreen() {
        // Check access permission first
        if (!CheckAccessRights(clsUser.enPermissions.pShowLoginRegister)) {
            return;
        }

        // Get login register records
        Vector<clsUser.clsLoginRegisterRecord> vLoginRegisterRecords = clsUser.getLoginRegisterList();
        String title = "Login Register List Screen";
        String subTitle = "(" + vLoginRegisterRecords.size() + ") Record(s)";

        // Padding to align with header
        String pad = String.format("%39s", ""); // same as header for consistency

        // Draw the table header
        System.out.println(pad + "_______________________________________________________" +
                "_________________________________________");
        System.out.println(pad + "| " + String.format("%-35s", "Date/Time") +
                "| " + String.format("%-20s", "UserName") +
                "| " + String.format("%-20s", "Password") +
                "| " + String.format("%-10s", "Permissions"));
        System.out.println(pad + "_______________________________________________________" +
                "_________________________________________");

        // If no records, show message
        if (vLoginRegisterRecords.isEmpty()) {
            System.out.println(pad + "  No Logins Available In the System!");
        } else {
            // Print each record
            for (clsUser.clsLoginRegisterRecord record : vLoginRegisterRecords) {
                printLoginRegisterRecordLine(record, pad);
            }
        }

        // Draw footer line
        System.out.println(pad + "_______________________________________________________" +
                "_________________________________________");
    }

    // Helper method to print one record line, aligned
    private static void printLoginRegisterRecordLine(clsUser.clsLoginRegisterRecord record, String pad) {
        System.out.println(pad + "| " +
                String.format("%-35s", record.dateTime) +
                "| " + String.format("%-20s", record.userName) +
                "| " + String.format("%-20s", record.password) +
                "| " + String.format("%-10d", record.permissions)
        );
    }
}
