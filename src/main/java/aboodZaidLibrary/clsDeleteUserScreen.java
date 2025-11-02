package aboodZaidLibrary;

public class clsDeleteUserScreen extends  clsScreen{
    private static void _PrintUser(clsUser user){
        System.out.print("\nUser Details:");
        System.out.print("\n___________________");
        System.out.println("\nUser FirstName : " + user.getFirstName());
        System.out.println("\nUser LasstName : " + user.getLastName());
        System.out.println("\nUser FullName : " + user.getFullName());
        System.out.println("\nUser Email : " + user.getEmail());
        System.out.println("\nUser UserName : " + user.getUserName());
        System.out.println("\nUser Password : " + user.getPassword());
        System.out.println("\nUser Permissions : " + user.getPermissions());
        System.out.print("___________________");
    }
    public static void showDeleteUserScreen(){
        String title="Delete User Screen";
        clsScreen._DrawScreenHeader(title);
        String userName;
        System.out.print("\nPlease enter Account Username: ");
        userName=clsInputValidate.readString();
        while (!clsUser.isUserExist(userName)){
            System.out.print("\nUsername is not found ,enter another one: ");
            userName=clsInputValidate.readString();
        }
        clsUser user=clsUser.find(userName);
        _PrintUser(user);
        System.out.print("\nAre you sure you want to delete this User y/n? ");
        char answer = 'n';
        answer=clsInputValidate.readChar();

        if (answer == 'y' || answer == 'Y')
        {
            if (user.delete())
            {
                System.out.println("\nBook Deleted Successfully :-)");
            }
            else
            {
                System.out.println("\nError book was not deleted");
            }
        }
    }

}
