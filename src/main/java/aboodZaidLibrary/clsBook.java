package aboodZaidLibrary;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class clsBook {
    private enMode _Mode;
    private String _Title;
    private String _Author;
    private String _ISBN;
    private boolean _MarkedForDelete = false;
    private enum enMode {
        EmptyMode(0),
        AddNewMode(1),
        UpdateMode(2);

        private final int value;

        enMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private static clsBook _ConvertLineToBookObject(String line,String separator){
        Vector<String>vBookData=new Vector<>(Arrays.asList(line.split(separator)));
        return new clsBook(enMode.UpdateMode,vBookData.get(0),vBookData.get(1),vBookData.get(2));
    }
    private static clsBook _ConvertLineToBookObject(String line){
        return _ConvertLineToBookObject(line,"#//#");
    }
    private static String _ConvertBookObjectToLine(clsBook book,String separator){
    String bookRecord="";
    bookRecord+=book._Title+separator;
    bookRecord+=book._Author+separator;
    bookRecord+=book._ISBN;
    return bookRecord;
    }
    private static String _ConvertBookObjectToLine(clsBook book){
        return _ConvertBookObjectToLine(book,"#//#");
    }
    private static clsBook _GetEmptyBookObject()
    {
        return new clsBook(enMode.EmptyMode, "", "", "");
    }
    private static Vector<clsBook> _LoadBooksDataFromFile(){
        Vector<clsBook> vBooks = new Vector<>();
        String fileName = "Books.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook  book = _ConvertLineToBookObject(line);
                vBooks.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vBooks;
    }
    private static void _SaveBooksDataToFile(Vector<clsBook> vBooks) {
        String fileName = "Books.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (clsBook u : vBooks) {
                if (!u.isMarkedForDeleted()) {
                    String dataLine = _ConvertBookObjectToLine(u);
                    bw.write(dataLine);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void _AddDataLineToFile(String stDataLine) {
        String fileName = "Books.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(stDataLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void _AddNew()
    {
        _AddDataLineToFile(_ConvertBookObjectToLine(this));
    }
    public clsBook(enMode mode, String title, String author, String isbn){
        _Mode = mode;
        _Title = title;
        _Author = author;
        _ISBN = isbn;
    }
    public boolean isEmpty() {
        return this._Mode == enMode.EmptyMode;
    }
    public boolean isMarkedForDeleted()
    {
        return _MarkedForDelete;
    }
    public void setTitle(String title) {
        _Title = title;
    }
    public void setAuthor(String author) {
        _Author = author;
    }
    public void setISBN(String isbn) {
        _ISBN = isbn;
    }
    public String getTitle(){
        return  _Title;
    }
    public String getAuthor(){
        return _Author;
    }
    public String getISBN(){
        return _ISBN;
    }
    public static clsBook findBookByTitle(String title) {
        String fileName = "Books.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getTitle().equals(title)) {
                    return book;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _GetEmptyBookObject();
    }
    public static clsBook findBookByAuthor(String author) {
        String fileName = "Books.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getAuthor().equals(author)) {
                    return book;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _GetEmptyBookObject();
    }
    public static clsBook findBookByISBN(String isbn) {
        String fileName = "Books.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getISBN().equals(isbn)) {
                    return book;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _GetEmptyBookObject();
    }
    public static Vector<clsBook> findBooksByTitle(String title) {
        Vector<clsBook> results = new Vector<>();
        String fileName = "Books.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getTitle().equals(title)) {
                    results.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
    public static Vector<clsBook> findBooksByAuthor(String author) {
        Vector<clsBook> results = new Vector<>();
        String fileName = "Books.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getAuthor().equals(author)) {
                    results.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
    public static Vector<clsBook> findBooksByISBN(String isbn) {
        Vector<clsBook> results = new Vector<>();
        String fileName = "Books.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsBook book = _ConvertLineToBookObject(line);
                if (book.getISBN().equals(isbn)) {
                    results.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
    public enum enSaveResults {
        svFaildEmptyObject(0),
        svSucceeded(1),
        svFaildBookExists(2);

        private final int value;

        enSaveResults(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public enSaveResults save() {
        switch (_Mode) {
            case EmptyMode:
                if (isEmpty()) {
                    return enSaveResults.svFaildEmptyObject;
                }
                break;

            case UpdateMode:
                return enSaveResults.svSucceeded;

            case AddNewMode:
                if (isBookExist(_ISBN)) {
                    return enSaveResults.svFaildBookExists;
                } else {
                    _AddNew();
                    _Mode = enMode.UpdateMode;
                    return enSaveResults.svSucceeded;
                }

            default:
                return enSaveResults.svFaildEmptyObject;
        }

        return enSaveResults.svFaildEmptyObject;
    }
    public static boolean isBookExist(String ispn) {
        clsBook book = findBookByISBN(ispn);
        return !book.isEmpty();
    }
    public static clsBook GetAddNewBookObject(String ispn) {
        return  new clsBook(enMode.AddNewMode,"","",ispn);
    }
    public static Vector <clsBook> getBooksList() {
        return _LoadBooksDataFromFile();
    }
    public boolean delete() {
        Vector<clsBook> vBooks = _LoadBooksDataFromFile();
        for (clsBook book : vBooks) {
            if (book.getISBN().equals(this._ISBN)) {
                book._MarkedForDelete = true;
                break;
            }
        }
        _SaveBooksDataToFile(vBooks);
        clsBook emtpyBook = _GetEmptyBookObject();
        this._Mode = emtpyBook._Mode;
        this._Title = emtpyBook._Title;
        this._Author = emtpyBook._Author;
        this._ISBN = emtpyBook._ISBN;
        this._MarkedForDelete = emtpyBook._MarkedForDelete;
        return true;
    }
}
