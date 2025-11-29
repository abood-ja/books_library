package aboodZaidLibrary;

import java.util.Vector;

public class clsCDsListScreen extends clsScreen {

    private static void _PrintCDRecordLine(clsCD cd, String pad) {
        System.out.println(pad + "| "
                + String.format("%-35s", cd.getTitle())
                + "| " + String.format("%-25s", cd.getArtist())
                + "| " + String.format("%-20s", cd.getCDID())
        );
    }

    public static void showCDsListScreen() {

        Vector<clsCD> vCDs = clsCD.getCDsList();
        String title = "CDs List Screen";
        String subTitle = "(" + vCDs.size() + ") CD(s)";

        clsScreen._DrawScreenHeader(title, subTitle);

        String pad = String.format("%20s", "");

        // Header
        System.out.println(pad + "__________________________________________________________________________________________");
        System.out.println(pad + "| " + String.format("%-35s", "Title")
                + "| " + String.format("%-25s", "Artist")
                + "| " + String.format("%-20s", "CD ID"));
        System.out.println(pad + "__________________________________________________________________________________________");

        if (vCDs.isEmpty()) {
            System.out.println(pad + "  No CDs Available In the System!");
        } else {
            for (clsCD cd : vCDs) {
                _PrintCDRecordLine(cd, pad);
            }
        }

        System.out.println(pad + "__________________________________________________________________________________________");
    }
}
