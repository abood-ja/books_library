package aboodZaidLibrary;

import java.util.Vector;

public class clsLoginRegisterScreen extends clsScreen {

    protected static void showLoginRegisterScreen() {

        Vector<clsUser.clsLoginRegisterRecord> vLoginRegisterRecords = clsUser.getLoginRegisterList();
        String title = "Login Register List Screen";
        String subTitle = "(" + vLoginRegisterRecords.size() + ") Record(s)";


        clsScreen._DrawScreenHeader(title, subTitle);


        String pad = String.format("%20s", "");


        System.out.println(pad + "______________________________________________________________________________________________");
        System.out.println(pad + "| " + String.format("%-35s", "Date/Time") +
                "| " + String.format("%-20s", "UserName") +
                "| " + String.format("%-20s", "Password") +
                "| " + String.format("%-10s", "Permissions"));
        System.out.println(pad + "______________________________________________________________________________________________");


        if (vLoginRegisterRecords.isEmpty()) {
            System.out.println(pad + "  No Logins Available In the System!");
        } else {

            for (clsUser.clsLoginRegisterRecord record : vLoginRegisterRecords) {
                printLoginRegisterRecordLine(record, pad);
            }
        }


        System.out.println(pad + "__________________________________________________________________________________________");
    }


    private static void printLoginRegisterRecordLine(clsUser.clsLoginRegisterRecord record, String pad) {
        System.out.println(pad + "| " +
                String.format("%-35s", record.dateTime) +
                "| " + String.format("%-20s", record.userName) +
                "| " + String.format("%-20s", record.password) +
                "| " + String.format("%-10d", record.permissions)
        );
    }
}
