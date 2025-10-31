package aboodZaidLibrary;

public class clsLoginScreen extends clsScreen{
private static boolean _Login(){
int failedLoginCount = 0;
    System.out.print("Enter Username: ");
    String userName;
    userName=clsInputValidate.readString();
    while(!clsUser.isUserExist(userName)){
        failedLoginCount++;
        if (failedLoginCount == 3) {
            System.out.println("\nYou are locked after 3 failed trials!! \n");
            new java.util.Scanner(System.in).nextLine();
            return false;
        }

        System.out.println("\nInvalid Username, You have " + (3 - failedLoginCount) + " Trie(s)\nEnter another one: ");
        userName = clsInputValidate.readString();
    }
    failedLoginCount = 0;
    System.out.print("Enter Password: ");
    String password = clsInputValidate.readString(); // Reads full line

    clsUser user = clsUser.find(userName, password);

    while (user.isEmpty()) {
        failedLoginCount++;
        if (failedLoginCount == 3) {
            System.out.println("\nYou are locked after 3 failed trials!! \n");
//            new java.util.Scanner(System.in).nextLine(); // Waits for Enter key like system("pause>0")
            return false;
        }
        System.out.println("\nWrong Password! You have " + (3 - failedLoginCount) + " Trie(s)\nEnter another one: ");
        password = clsInputValidate.readString();
        user = clsUser.find(userName, password);
    }
    clsUserSession.currentUser=user;
    clsUserSession.currentUser.registerLogin();
    clsMainScreen.showMainMenu();
    return true;
}
public static boolean showLoginScreen() {
        // Clear console (works in terminal, may not clear Eclipse console)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        String title = "\t  Login Screen";
        _DrawScreenHeader(title);

        return _Login();
}
}
