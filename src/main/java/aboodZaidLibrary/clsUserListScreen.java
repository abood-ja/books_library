package aboodZaidLibrary;

import java.util.Vector;

public class clsUserListScreen extends clsScreen {

    private static void _PrintUserRecordLine(clsUser user, String pad) {
        System.out.println(pad + "| " +
                String.format("%-15s", user.getUserName()) +
                "| " + String.format("%-25s", user.getFullName()) +
                "| " + String.format("%-15s", user.getPhone()) +
                "| " + String.format("%-30s", user.getEmail()) +
                "| " + String.format("%-15s", user.getPassword()) +
                "| " + String.format("%-10s", user.getPermissions())
        );
    }

    public static void showUserListScreen() {
        Vector<clsUser> vUsers = clsUser.getUsersList();
        String title = "Users List Screen";
        String subTitle = "(" + vUsers.size() + ") User(s)";

        clsScreen._DrawScreenHeader(title, subTitle);

        String pad = String.format("%20s", "");


        System.out.println(pad + "______________________________________________________________________________________________________________________________");
        System.out.println(pad + "| " + String.format("%-15s", "User Name") +
                "| " + String.format("%-25s", "Full Name") +
                "| " + String.format("%-15s", "Phone") +
                "| " + String.format("%-30s", "Email") +
                "| " + String.format("%-15s", "Password") +
                "| " + String.format("%-10s", "Permissions"));
        System.out.println(pad + "______________________________________________________________________________________________________________________________");

        if (vUsers.isEmpty()) {
            System.out.println(pad + "  No Users Available In the System!");
        } else {
            for (clsUser user : vUsers) {
                _PrintUserRecordLine(user, pad);
            }
        }

        System.out.println(pad + "______________________________________________________________________________________________________________________________");
    }
}
