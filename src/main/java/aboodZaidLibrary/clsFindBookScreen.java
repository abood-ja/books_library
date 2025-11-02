package aboodZaidLibrary;

import java.util.Vector;

public class clsFindBookScreen extends clsScreen{
    private static void _PrintBook(clsBook book){
        System.out.print("\nBook Details:");
        System.out.print("\n___________________");
        System.out.println("\nBook Title  : " + book.getTitle());
        System.out.println("\nBook Author : " + book.getAuthor());
        System.out.println("\nBook ISPN   : " + book.getISBN());
        System.out.print("___________________");
    }
    private static void _ShowSearchResults(Vector<clsBook> books) {
        if (books == null || books.isEmpty()) {
            System.out.println("\nNo books found :-(");
        } else {
            System.out.println("\nBook(s) Found :-)");
            System.out.println("___________________");
            for (clsBook book : books) {
                _PrintBook(book);
                System.out.println("___________________");
            }
        }
    }
    public static void showFindBookScreen(){
        int findBy;
        String title="Find Book Screen";
        clsScreen._DrawScreenHeader(title);
        System.out.print("\nFind By: [1] Title or [2] Author or [3] ISBN: ? ");
        findBy=clsInputValidate.readIntNumberBetween(1,3);
        Vector<clsBook> books;
        if(findBy==1){
            String bookTitle;
            System.out.print("\nPlease Enter The Title of The Book: ");
            bookTitle=clsInputValidate.readString();
            books=clsBook.findBooksByTitle(bookTitle);
            _ShowSearchResults(books);

        } else if (findBy==2) {
            String bookAuthor;
            System.out.print("\nPlease Enter The Name of The Author: ");
            bookAuthor=clsInputValidate.readString();
             books=clsBook.findBooksByAuthor(bookAuthor);
            _ShowSearchResults(books);

        }
        else if(findBy==3) {
            String bookISPN;
            System.out.print("\nPlease Enter The ISPN of The Book: ");
            bookISPN=clsInputValidate.readString();
             books=clsBook.findBooksByISBN(bookISPN);
            _ShowSearchResults(books);

        }
    }
}
