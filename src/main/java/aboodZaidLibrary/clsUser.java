package aboodZaidLibrary;

import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public class clsUser extends clsPerson {

    // ===== Enums =====
    public enum enMode {
        EmptyMode(0),
        UpdateMode(1),
        AddNewMode(2);

        private final int value;
        enMode(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public enum enPermissions {
        eAll(-1),
        pAddNewBook(1),
        pDeleteBook(2),
        pManageUsers(4);

        private final int value;
        enPermissions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public enum enSaveResults {
        svFaildEmptyObject(0),
        svSucceeded(1),
        svFaildUserExists(2);

        private final int value;
        enSaveResults(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    // ===== Properties =====
    private enMode _Mode;
    private String _UserName;
    private String _Password;
    private int _Permissions;
    private boolean _MarkedForDelete = false;

    // ===== Internal Login Record Class =====
    public static class clsLoginRegisterRecord {
        public String dateTime;
        public String userName;
        public String password;
        public int permissions;
    }

    // ===== Constructors =====
    public clsUser(enMode mode, String firstName, String lastName, String email, String phone,
                   String userName, String password, int permissions) {
        super(firstName, lastName, email, phone);
        this._Mode = mode;
        this._UserName = userName;
        this._Password = password;
        this._Permissions = permissions;
    }

    private static clsUser _GetEmptyUserObject() {
        return new clsUser(enMode.EmptyMode, "", "", "", "", "", "", 0);
    }

    // ===== Convertions between file lines and objects =====
    private static clsUser _ConvertLineToUserObject(String line) {
        String separator = "#//#";
        int decryptionKey = 2;
        Vector<String> vUserData = new Vector<>(Arrays.asList(line.split(separator)));

        // لحماية من سطور ناقصة
        while (vUserData.size() < 7) vUserData.add("");

        return new clsUser(
                enMode.UpdateMode,
                vUserData.get(0),
                vUserData.get(1),
                vUserData.get(2),
                vUserData.get(3),
                vUserData.get(4),
                clsEncryption.decryptText(vUserData.get(5), decryptionKey),
                Integer.parseInt(vUserData.get(6))
        );
    }

    private static String _ConvertUserObjectToLine(clsUser user) {
        String separator = "#//#";
        int encryptionKey = 2;
        StringBuilder line = new StringBuilder();

        line.append(user.getFirstName()).append(separator)
                .append(user.getLastName()).append(separator)
                .append(user.getEmail()).append(separator)
                .append(user.getPhone()).append(separator)
                .append(user.getUserName()).append(separator)
                .append(clsEncryption.encryptText(user.getPassword(), encryptionKey)).append(separator)
                .append(user.getPermissions());

        return line.toString();
    }

    // ===== File Operations =====
    private static Vector<clsUser> _LoadUsersDataFromFile() {
        Vector<clsUser> vUsers = new Vector<>();
        String fileName = "Users.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsUser user = _ConvertLineToUserObject(line);
                vUsers.add(user);
            }
        } catch (IOException e) {
            // لا مشكلة إن لم يوجد الملف بعد
        }
        return vUsers;
    }

    private static void _SaveUsersDataToFile(Vector<clsUser> vUsers) {
        String fileName = "Users.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (clsUser u : vUsers) {
                if (!u.isMarkedForDeleted()) {
                    String dataLine = _ConvertUserObjectToLine(u);
                    bw.write(dataLine);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void _AddDataLineToFile(String stDataLine) {
        String fileName = "Users.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(stDataLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _AddNew() { _AddDataLineToFile(_ConvertUserObjectToLine(this)); }

    private void _Update() {
        Vector<clsUser> vUsers = _LoadUsersDataFromFile();
        for (int i = 0; i < vUsers.size(); i++) {
            clsUser user = vUsers.get(i);
            if (user.getUserName().equals(this._UserName)) {
                vUsers.set(i, this);
            }
        }
        _SaveUsersDataToFile(vUsers);
    }

    // ===== Login / Register =====
    private static clsLoginRegisterRecord _ConvertLoginRegisterLineToRecord(String line) {
        String separator = "#//#";
        int decryptionKey = 2;
        Vector<String> loginRegisterDataLine = new Vector<>(Arrays.asList(line.split(separator)));

        clsLoginRegisterRecord rec = new clsLoginRegisterRecord();
        rec.dateTime = loginRegisterDataLine.get(0);
        rec.userName = loginRegisterDataLine.get(1);
        rec.password = clsEncryption.decryptText(loginRegisterDataLine.get(2), decryptionKey);
        rec.permissions = Integer.parseInt(loginRegisterDataLine.get(3));
        return rec;
    }

    private String _PrepareLoginRecord() {
        int encryptionKey = 2;
        String separator = "#//#";
        return clsDate.getSystemDateTimeString() + separator
                + _UserName + separator
                + clsEncryption.encryptText(_Password, encryptionKey) + separator
                + _Permissions;
    }

    public void registerLogin() {
        String fileName = "LoginRegister.txt";
        String dataLine = _PrepareLoginRecord();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(dataLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== Public Methods =====
    public boolean isEmpty() { return this._Mode == enMode.EmptyMode; }

    public boolean isMarkedForDeleted() { return _MarkedForDelete; }

    public void setUserName(String userName) { this._UserName = userName; }

    public String getUserName() { return this._UserName; }

    public boolean isAdmin() { return this._Permissions == -1; }

    public void setPassword(String password) { this._Password = password; }

    public String getPassword() { return this._Password; }

    public void setPermissions(int permissions) { this._Permissions = permissions; }

    public int getPermissions() { return this._Permissions; }

    // ===== Static finders =====
    public static clsUser find(String userName) {
        String fileName = "Users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsUser user = _ConvertLineToUserObject(line);
                if (user.getUserName().equals(userName)) return user;
            }
        } catch (IOException e) { }
        return _GetEmptyUserObject();
    }

    public static clsUser find(String userName, String password) {
        String fileName = "Users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                clsUser user = _ConvertLineToUserObject(line);
                if (user.getUserName().equals(userName)
                        && user.getPassword().equals(password))
                    return user;
            }
        } catch (IOException e) { }
        return _GetEmptyUserObject();
    }

    public static boolean isUserExist(String userName) {
        clsUser user = find(userName);
        return !user.isEmpty();
    }

    public static clsUser GetAddNewUserObject(String UserName) {
        return new clsUser(enMode.AddNewMode, "", "", "", "", UserName, "", 0);
    }

    public static Vector<clsUser> getUsersList() { return _LoadUsersDataFromFile(); }

    // ===== Save / Delete =====
    public enSaveResults save() {
        switch (_Mode) {
            case EmptyMode:
                if (isEmpty()) return enSaveResults.svFaildEmptyObject;
                break;

            case UpdateMode:
                _Update();
                return enSaveResults.svSucceeded;

            case AddNewMode:
                if (clsUser.isUserExist(_UserName))
                    return enSaveResults.svFaildUserExists;
                else {
                    _AddNew();
                    _Mode = enMode.UpdateMode;
                    return enSaveResults.svSucceeded;
                }

            default:
                return enSaveResults.svFaildEmptyObject;
        }
        return enSaveResults.svFaildEmptyObject;
    }

    public boolean delete() {
        Vector<clsUser> vUsers = _LoadUsersDataFromFile();

        for (clsUser user : vUsers) {
            if (user.getUserName().equals(this._UserName)) {
                user._MarkedForDelete = true;
                break;
            }
        }
        _SaveUsersDataToFile(vUsers);

        // Reset
        clsUser emptyUser = _GetEmptyUserObject();
        this._Mode = emptyUser._Mode;
        this._UserName = emptyUser._UserName;
        this._Password = emptyUser._Password;
        this._Permissions = emptyUser._Permissions;
        this._MarkedForDelete = emptyUser._MarkedForDelete;
        return true;
    }

    // ===== Access Permissions =====
    public boolean checkAccessPermission(enPermissions permission) {
        int userPermissions = this.getPermissions();
        if (userPermissions == enPermissions.eAll.getValue()) return true;
        return (permission.getValue() & userPermissions) == permission.getValue();
    }

    // ===== Login Register View =====
    public static Vector<clsLoginRegisterRecord> getLoginRegisterList() {
        Vector<clsLoginRegisterRecord> vLoginRegisterRecords = new Vector<>();
        String fileName = "LoginRegister.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                vLoginRegisterRecords.add(_ConvertLoginRegisterLineToRecord(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vLoginRegisterRecords;
    }
    // ===== Borrowing & Fines Fields =====
    private double _FineBalance = 0.0;   // total unpaid fines
    private static final String LOANS_FILE = "Loans.txt";
    private static final String SEP = "#//#";

    public double getFineBalance() { return _FineBalance; }
    public void addFine(double amount) { _FineBalance += amount; }
    public void payFine(double amount) {
        if (amount <= 0) return;
        _FineBalance -= amount;
        if (_FineBalance < 0) _FineBalance = 0;
    }

    // ====== Check if user has overdue loans ======
    public boolean hasOverdue() {
        Path f = Paths.get(LOANS_FILE);
        if (!Files.exists(f)) return false;

        LocalDate today = LocalDate.now();

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(Pattern.quote(SEP), -1);
                if (p.length >= 5) {
                    String username = p[1].trim();
                    LocalDate due = LocalDate.parse(p[3].trim());
                    boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                    if (!returned &&
                            username.equalsIgnoreCase(this.getUserName()) &&
                            today.isAfter(due)) {

                        long days = ChronoUnit.DAYS.between(due, today);
                        return days > 0;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    // ====== Check unpaid fines ======
    public boolean hasUnpaidFines() {
        return _FineBalance > 0;
    }

    // ====== Check borrow permission ======
    public boolean canBorrow() {
        if (hasOverdue()) {
            System.out.println("❌ You cannot borrow: You have overdue items.");
            return false;
        }
        if (hasUnpaidFines()) {
            System.out.println("❌ You cannot borrow: You have unpaid fines = " + _FineBalance + " NIS.");
            return false;
        }
        return true;
    }
    // ====== Check if user can be deleted ======
    public boolean canBeDeleted() {
        String usernameKey = this.getUserName().trim().toLowerCase();
        Path f = Paths.get("Loans.txt");

        // فحص الغرامات
        if (this.hasUnpaidFines()) {
            return false;
        }

        if (!Files.exists(f)) {
            return true; // ما في إقراض أصلاً
        }

        try {
            List<String> lines = Files.readAllLines(f);

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split("#//#", -1);

                // عندنا نوعين:
                // Books:   isbn#//#username#//#borrow#//#due#//#returned
                // CDs:     CD#//#cdid#//#username#//#borrow#//#due#//#returned

                if (p.length < 5) continue;

                boolean returned = Boolean.parseBoolean(p[p.length - 1].trim());

                String loanUser = "";
                if (p[0].equals("CD"))      // CD loan format
                    loanUser = p[2].trim().toLowerCase();
                else                        // Book loan format
                    loanUser = p[1].trim().toLowerCase();

                if (loanUser.equals(usernameKey) && !returned) {
                    return false; // عنده Item غير مُعاد
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true; // كل شيء تمام
    }

}
