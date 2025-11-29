package aboodZaidLibrary;

public class clsDeleteCDScreen extends clsScreen {

    private static void _PrintCD(clsCD cd) {
        System.out.println("\nCD Details:");
        System.out.println("______________________");
        System.out.println("Title   : " + cd.getTitle());
        System.out.println("Artist  : " + cd.getArtist());
        System.out.println("CD ID   : " + cd.getCDID());
        System.out.println("______________________");
    }

    public static void showDeleteCDScreen() {

        if(!clsScreen.CheckAccessRights(clsUser.enPermissions.pDeleteBook)) {
            return;
        }

        _DrawScreenHeader("\t\tDelete CD");

        System.out.print("\nEnter CD ID: ");
        String cdid = clsInputValidate.readString();

        while (!clsCD.isCDExist(cdid)) {
            System.out.print("\nCD not found, enter another one: ");
            cdid = clsInputValidate.readString();
        }

        clsCD cd = clsCD.findCDByID(cdid);

        _PrintCD(cd);

        System.out.print("\nAre you sure you want to delete this CD y/n? ");
        char answer = clsInputValidate.readChar();

        if (answer == 'y' || answer == 'Y') {
            if (cd.delete())
                System.out.println("\nCD Deleted Successfully!");
            else
                System.out.println("\nError deleting CD.");
        }
    }
}
