package aboodZaidLibrary;

public class clsDeleteBookScreen extends clsScreen{
    private static void _PrintBook(clsBook book){
        System.out.print("\nBook Details:");
        System.out.print("\n___________________");
        System.out.println("\nBook Title  : " + book.getTitle());
        System.out.println("\nBook Author : " + book.getAuthor());
        System.out.println("\nBook ISPN   : " + book.getISBN());
        System.out.print("___________________");
    }
    public static void showDeleteBookScreen(){
        if(!clsScreen.CheckAccessRights(clsUser.enPermissions.pDeleteBook)){
            return;
        }
        String title="Delete Book Screen";
        clsScreen._DrawScreenHeader(title);
        String ispn;
        System.out.print("\nPlease enter Book ISPN: ");
        ispn=clsInputValidate.readString();
        while (!clsBook.isBookExist(ispn))
        {
            System.out.print("\nISPN is not found, enter another one: ");
            ispn=clsInputValidate.readString();
        }
        clsBook book=clsBook.findBookByISBN(ispn);
        _PrintBook(book);
        System.out.print("\nAre you sure you want to delete this book y/n? ");
        char answer = 'n';
        answer=clsInputValidate.readChar();
        if (answer == 'y' || answer == 'Y') {
            if (book.delete()) {
                System.out.println("\nBook Deleted Successfully :-)");
            } else {
                System.out.println("\nError book was not deleted");
            }
        }
    }
}
