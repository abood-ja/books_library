package aboodZaidLibrary;

public class clsAddNewBookScreen extends  clsScreen{
    private static void readBookInfo(clsBook book){
        System.out.print("\nPlease enter Book Title: ");
        book.setTitle(clsInputValidate.readString());
        System.out.print("\nPlease enter Book Author: ");
        book.setAuthor(clsInputValidate.readString());
    }
    private static void printBook(clsBook book){
        System.out.print("\nBook Details:");
        System.out.print("\n___________________");
        System.out.println("\nBook Title  : " + book.getTitle());
        System.out.println("\nBook Author : " + book.getAuthor());
        System.out.println("\nBook ISPN   : " + book.getISBN());
        System.out.print("___________________");
    }
    public static void showAddNewBookScreen(){
        if(!clsScreen.CheckAccessRights(clsUser.enPermissions.pAddNewBook)){
            return;
        }
        String title="Add New Book Screen";
        clsScreen._DrawScreenHeader(title);
        String ispn;
        System.out.print("\nPlease enter Book ISPN: ");
        ispn=clsInputValidate.readString();
        while(clsBook.isBookExist(ispn)){
            System.out.print("\nISPN is already used, choose another one: ");
            ispn=clsInputValidate.readString();
        }
        clsBook newBook=clsBook.GetAddNewBookObject(ispn);
        readBookInfo(newBook);
        clsBook.enSaveResults saveResult;
        saveResult=newBook.save();
        switch (saveResult){
            case svSucceeded:
                System.out.print("\nBook Added successfully :-)\n");
                printBook(newBook);
                break;
            case svFaildEmptyObject:
                System.out.print("\nError Book was not saved because it's empty");
                break;
            case svFaildBookExists:
                System.out.print("\nError Book was not saved because ISPN is used!\n");
                break;
        }
    }
}
