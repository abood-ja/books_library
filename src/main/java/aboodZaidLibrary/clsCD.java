package aboodZaidLibrary;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class clsCD {

    private enMode _Mode;
    private String _Title;
    private String _Artist;
    private String _CDID;
    private boolean _MarkedForDelete = false;

    private enum enMode {
        EmptyMode(0),
        AddNewMode(1),
        UpdateMode(2);

        private final int value;

        enMode(int value) { this.value = value; }

        public int getValue() {
            return value;
        }
    }

    private static clsCD _GetEmptyCDObject() {
        return new clsCD(enMode.EmptyMode, "", "", "");
    }

    // ====== Constructors ======
    public clsCD(enMode mode, String title, String artist, String cdid) {
        _Mode = mode;
        _Title = title;
        _Artist = artist;
        _CDID = cdid;
    }

    // ====== File Operations ======
    private static String _ConvertCDObjectToLine(clsCD cd) {
        return cd._Title + "#//#" + cd._Artist + "#//#" + cd._CDID;
    }

    private static clsCD _ConvertLineToCDObject(String line) {
        Vector<String> vCDData = new Vector<>(Arrays.asList(line.split("#//#")));
        return new clsCD(enMode.UpdateMode, vCDData.get(0), vCDData.get(1), vCDData.get(2));
    }

    private static Vector<clsCD> _LoadCDsDataFromFile() {
        Vector<clsCD> vCDs = new Vector<>();
        String fileName = "CDs.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                clsCD cd = _ConvertLineToCDObject(line);
                vCDs.add(cd);
            }
        } catch (IOException e) {
            // ملف غير موجود؟ عادي
        }

        return vCDs;
    }

    private static void _SaveCDsDataToFile(Vector<clsCD> vCDs) {
        String fileName = "CDs.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (clsCD cd : vCDs) {
                if (!cd._MarkedForDelete) {
                    bw.write(_ConvertCDObjectToLine(cd));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void _AddDataLineToFile(String stDataLine) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("CDs.txt", true))) {
            bw.write(stDataLine);
            bw.newLine();
        } catch (IOException e) { }
    }

    private void _AddNew() {
        _AddDataLineToFile(_ConvertCDObjectToLine(this));
    }

    // ====== Public Properties ======
    public String getTitle() { return _Title; }
    public String getArtist() { return _Artist; }
    public String getCDID() { return _CDID; }

    public void setTitle(String title) { _Title = title; }
    public void setArtist(String artist) { _Artist = artist; }
    public void setCDID(String id) { _CDID = id; }

    public boolean isEmpty() { return _Mode == enMode.EmptyMode; }

    public boolean delete() {
        Vector<clsCD> vCDs = _LoadCDsDataFromFile();

        for (clsCD cd : vCDs) {
            if (cd._CDID.equals(this._CDID)) {
                cd._MarkedForDelete = true;
                break;
            }
        }

        _SaveCDsDataToFile(vCDs);

        clsCD emptyCD = _GetEmptyCDObject();
        this._Mode = emptyCD._Mode;
        this._Title = emptyCD._Title;
        this._Artist = emptyCD._Artist;
        this._CDID = emptyCD._CDID;

        return true;
    }

    // ====== Find ======
    public static clsCD findCDByID(String id) {
        Vector<clsCD> vCDs = _LoadCDsDataFromFile();

        for (clsCD cd : vCDs) {
            if (cd.getCDID().equals(id)) return cd;
        }

        return _GetEmptyCDObject();
    }

    public static Vector<clsCD> findCDsByTitle(String title) {
        Vector<clsCD> result = new Vector<>();
        Vector<clsCD> vCDs = _LoadCDsDataFromFile();

        for (clsCD cd : vCDs) {
            if (cd.getTitle().equals(title)) result.add(cd);
        }

        return result;
    }

    public static Vector<clsCD> findCDsByArtist(String artist) {
        Vector<clsCD> result = new Vector<>();
        Vector<clsCD> vCDs = _LoadCDsDataFromFile();

        for (clsCD cd : vCDs) {
            if (cd.getArtist().equals(artist)) result.add(cd);
        }

        return result;
    }

    public static boolean isCDExist(String id) {
        return !findCDByID(id).isEmpty();
    }

    public static clsCD GetAddNewCDObject(String id) {
        return new clsCD(enMode.AddNewMode, "", "", id);
    }

    public enum enSaveResults {
        svFailedEmptyObject,
        svSucceeded,
        svFailedCDExists
    }

    public enSaveResults save() {
        switch (_Mode) {

            case EmptyMode:
                return enSaveResults.svFailedEmptyObject;

            case UpdateMode:
                return enSaveResults.svSucceeded;

            case AddNewMode:
                if (isCDExist(_CDID))
                    return enSaveResults.svFailedCDExists;
                else {
                    _AddNew();
                    _Mode = enMode.UpdateMode;
                    return enSaveResults.svSucceeded;
                }
        }
        return enSaveResults.svFailedEmptyObject;
    }

    public static Vector<clsCD> getCDsList() { return _LoadCDsDataFromFile(); }
}
