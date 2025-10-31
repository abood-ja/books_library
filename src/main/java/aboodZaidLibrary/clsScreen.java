package aboodZaidLibrary;

public class clsScreen {

    protected static void _DrawScreenHeader(String title, String subTitle) {
        // Consistent padding + 2 extra spaces to center it better
        String pad = String.format("%39s", ""); // 37 + 2 spaces

        System.out.println(pad + "______________________________________");
        System.out.println(pad + "       " + title); // title with small indent

        if (subTitle != null && !subTitle.isEmpty()) {
            System.out.println(pad + "   " + subTitle); // subtitle aligned under title
        }

        System.out.println(pad + "______________________________________");

        if (!clsUserSession.currentUser.isEmpty()) {
            System.out.println(pad + "User: " + clsUserSession.currentUser.getFullName());
        }

        System.out.println(pad + "Date: " + clsDate.DateToString(new clsDate()) + "\n");
    }


    protected static void _DrawScreenHeader(String title) {
        _DrawScreenHeader(title, "");  
    }
    protected static boolean CheckAccessRights(clsUser.enPermissions permission) {
        if (!clsUserSession.currentUser.checkAccessPermission(permission)) {
            // Same padding as _DrawScreenHeader (37 + 2 spaces for centering)
            String pad = String.format("%39s", "");

            System.out.println(pad + "______________________________________");
            System.out.println(pad + "  Access Denied! Contact your Admin.");
            System.out.println(pad + "______________________________________\n");

            return false;
        } else {
            return true;
        }
    }
}
