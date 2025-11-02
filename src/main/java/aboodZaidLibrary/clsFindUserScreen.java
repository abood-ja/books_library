package aboodZaidLibrary;

public class clsFindUserScreen extends clsScreen{
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
    public static void showFindUserScreen(){
        String title="Add New User Screen";
        clsScreen._DrawScreenHeader(title);
        String userName;
        System.out.print("\nPlease enter Account Username: ");
        userName=clsInputValidate.readString();
        while (!clsUser.isUserExist(userName)){
            System.out.print("\nUsername is not found ,enter another one: ");
            userName=clsInputValidate.readString();
        }
        clsUser user=clsUser.find(userName);
        if(!user.isEmpty()){
            System.out.print("\nUser Found :-)\n");
            _PrintUser(user);
        }
        else{
            System.out.print("\nUser Was not Found :-)\\n");
        }
    }
}
