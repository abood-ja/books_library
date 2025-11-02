package aboodZaidLibrary;

public class clsAddUserScreen extends  clsScreen{
    private static int _ReadPermissionsToSet(){
        int permissions = 0;
        char answer = 'n';
        System.out.print("\nDo you want to give full access y/n? ");
        answer=clsInputValidate.readChar();
        if (answer == 'y' || answer == 'Y')
        {
            return -1;
        }
        System.out.print("\nDo you want to give access to : \n ");
        System.out.print("\nAdd New Book? y/n? ");
        answer = clsInputValidate.readChar();
        if (answer == 'y' || answer == 'Y') {
            permissions += clsUser.enPermissions.pAddNewBook.getValue();
        }
        System.out.print("\nDelete Book? y/n? ");
        answer = clsInputValidate.readChar();
        if (answer == 'y' || answer == 'Y') {
            permissions += clsUser.enPermissions.pDeleteBook.getValue();
        }
        System.out.print("\nManage Users? y/n? ");
        answer = clsInputValidate.readChar();
        if (answer == 'y' || answer == 'Y') {
            permissions += clsUser.enPermissions.pManageUsers.getValue();
        }
        return permissions;

    }
    private static void _ReadUserInfo(clsUser user){
        System.out.print("\nEnter FirstName: ");
        user.setFirstName(clsInputValidate.readString());
        System.out.print("\nEnter LastName: ");
        user.setLastName(clsInputValidate.readString());
        System.out.print("\nEnter Email: ");
        user.setEmail(clsInputValidate.readString());
        System.out.print("\nEnter Phone Number: ");
        user.setPhone(clsInputValidate.readString());
        System.out.print("\nEnter Password: ");
        user.setPassword(clsInputValidate.readString());
        System.out.print("\nEnter Permessions: ");
        user.setPermissions(_ReadPermissionsToSet());
    }
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
    public static void showAddUserScreen(){
        String title="Add New User Screen";
        clsScreen._DrawScreenHeader(title);
        String userName;
        System.out.print("\nPlease enter Account Username: ");
        userName=clsInputValidate.readString();
        while (clsUser.isUserExist(userName)){
            System.out.print("\nThis Username is already used, choose another one: ");
            userName=clsInputValidate.readString();
        }
        clsUser newUser=clsUser.GetAddNewUserObject(userName);
        _ReadUserInfo(newUser);
        clsUser.enSaveResults saveResult;
        saveResult = newUser.save();
        switch (saveResult){
            case svSucceeded:
                System.out.print("\nUser Added successfully :-)\n");
                _PrintUser(newUser);
                break;
            case svFaildEmptyObject:
                System.out.print("\nError User was not saved because it's empty");
                break;
            case svFaildUserExists:
                System.out.print("\nError User was not saved because UserName is used!\n");
                break;
        }
    }
}
