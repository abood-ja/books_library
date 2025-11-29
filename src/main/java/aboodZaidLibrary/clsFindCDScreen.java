package aboodZaidLibrary;

import java.util.Vector;

public class clsFindCDScreen extends clsScreen {

    private static void _PrintCD(clsCD cd) {
        System.out.print("\nCD Details:");
        System.out.print("\n___________________");
        System.out.println("\nTitle   : " + cd.getTitle());
        System.out.println("Artist  : " + cd.getArtist());
        System.out.println("CD ID   : " + cd.getCDID());
        System.out.print("___________________");
    }

    private static void _ShowSearchResults(Vector<clsCD> cds) {
        if (cds == null || cds.isEmpty()) {
            System.out.println("\nNo CDs found :-(");
        } else {
            System.out.println("\nCD(s) Found:");
            System.out.println("___________________");
            for (clsCD cd : cds) {
                _PrintCD(cd);
                System.out.println("___________________");
            }
        }
    }

    public static void showFindCDScreen() {

        int findBy;
        String title = "Find CD Screen";
        clsScreen._DrawScreenHeader(title);

        System.out.print("\nFind By: [1] Title  [2] Artist  [3] CD ID: ");
        findBy = clsInputValidate.readIntNumberBetween(1, 3);

        Vector<clsCD> cds;

        if (findBy == 1) {
            String cdTitle;
            System.out.print("\nEnter CD Title: ");
            cdTitle = clsInputValidate.readString();
            cds = clsCD.findCDsByTitle(cdTitle);
            _ShowSearchResults(cds);

        } else if (findBy == 2) {
            String artist;
            System.out.print("\nEnter Artist Name: ");
            artist = clsInputValidate.readString();
            cds = clsCD.findCDsByArtist(artist);
            _ShowSearchResults(cds);

        } else {
            String id;
            System.out.print("\nEnter CD ID: ");
            id = clsInputValidate.readString();
            Vector<clsCD> result = new Vector<>();
            clsCD cd = clsCD.findCDByID(id);
            if (!cd.isEmpty()) result.add(cd);
            _ShowSearchResults(result);
        }
    }
}
