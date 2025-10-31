package aboodZaidLibrary;

public class clsScreen {
	
    protected static void _DrawScreenHeader(String title, String subTitle) {
        System.out.println("\t\t\t\t\t______________________________________");
        System.out.println("\n\n\t\t\t\t\t  " + title);

        if (subTitle != null && !subTitle.isEmpty()) {
            System.out.println("\n\t\t\t\t\t  " + subTitle);
        }

        System.out.println("\n\t\t\t\t\t______________________________________\n\n");

        if (!clsUserSession.currentUser.isEmpty()) {  // Assuming currentUser is a static field in clsUser
            System.out.println("\n\t\t\t\t\tUser: " + clsUserSession.currentUser.getFullName() + "\n");
        }

        System.out.println("\t\t\t\t\tDate: " + clsDate.DateToString(new clsDate()) + "\n\n");
    }

    protected static void _DrawScreenHeader(String title) {
        _DrawScreenHeader(title, "");  
    }
}
