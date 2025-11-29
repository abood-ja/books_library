package aboodZaidLibrary;

public class clsDeleteUserScreen extends clsScreen {

    private static void _PrintUser(clsUser user) {
        System.out.print("\nUser Details:");
        System.out.print("\n___________________");
        System.out.println("\nUser FirstName : " + user.getFirstName());
        System.out.println("\nUser LastName : " + user.getLastName());
        System.out.println("\nUser FullName : " + user.getFullName());
        System.out.println("\nUser Email : " + user.getEmail());
        System.out.println("\nUser UserName : " + user.getUserName());
        System.out.println("\nUser Permissions : " + user.getPermissions());
        System.out.print("\n___________________\n");
    }

    public static void showDeleteUserScreen() {
        String title = "Delete User Screen";
        clsScreen._DrawScreenHeader(title);

        System.out.print("\nEnter Username to delete: ");
        String username = clsInputValidate.readString().trim();

        while (!clsUser.isUserExist(username)) {
            System.out.print("\n❌ Username not found! Enter another: ");
            username = clsInputValidate.readString().trim();
        }

        clsUser user = clsUser.find(username);
        _PrintUser(user);

        // ====== أهم خطوة ======
        if (!user.canBeDeleted()) {
            System.out.println("\n❌ Cannot delete user: They have unpaid fines or active/unreturned loans.");
            return;
        }

        System.out.print("\nAre you sure you want to delete this user? [y/n]: ");
        char ans = clsInputValidate.readChar();

        if (ans == 'y' || ans == 'Y') {
            if (user.delete()) {
                System.out.println("\n✔ User Deleted Successfully!");
            } else {
                System.out.println("\n❌ Error: User was not deleted.");
            }
        }
    }
}
