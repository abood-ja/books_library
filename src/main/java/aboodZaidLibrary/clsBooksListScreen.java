package aboodZaidLibrary;

import java.util.Vector;

public class clsBooksListScreen extends clsScreen {

    private static void printBookRecordLine(clsBook book, String pad) {
        System.out.println(pad + "| " +
                String.format("%-35s", book.getTitle()) +
                "| " + String.format("%-25s", book.getAuthor()) +
                "| " + String.format("%-20s", book.getISBN())
        );
    }

    public static void showBooksListScreen() {
        Vector<clsBook> vBooks = clsBook.getBooksList();
        String title = "Books List Screen";
        String subTitle = "(" + vBooks.size() + ") Book(s)";


        clsScreen._DrawScreenHeader(title, subTitle);


        String pad = String.format("%20s", "");

        // Draw the table header
        System.out.println(pad + "__________________________________________________________________________________________");
        System.out.println(pad + "| " + String.format("%-35s", "Title") +
                "| " + String.format("%-25s", "Author") +
                "| " + String.format("%-20s", "ISBN"));
        System.out.println(pad + "__________________________________________________________________________________________");


        if (vBooks.isEmpty()) {
            System.out.println(pad + "  No Books Available In the System!");
        } else {
            for (clsBook book : vBooks) {
                printBookRecordLine(book, pad);
            }
        }


        System.out.println(pad + "__________________________________________________________________________________________");
    }
}
