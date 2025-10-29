package aboodZaidLibrary;
import java.util.Vector;
import java.util.Arrays;
import java.io.*;
public class clsUser extends clsPerson{
	private enum enMode {
	    EmptyMode(0),
	    UpdateMode(1),
	    AddNewMode(2);
	    private final int value;

	    enMode(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	private enMode _Mode;
	private String _UserName;
	private String _Password;
	private int _Permissions;
	private boolean _MarkedForDelete=false;
	private boolean _IsAdmin=false;
	public static class clsLoginRegisterRecord {
	        public String dateTime;
	        public String userName;
	        public String password;
	        public int permissions;
 }
	private static clsLoginRegisterRecord _ConvertLoginRegisterLineToRecord(String line)
	{
		String seperator = "#//#";
		int decryptionKey=2;
		clsLoginRegisterRecord loginRegisterRecord=new clsLoginRegisterRecord();
		Vector<String> loginRegisterDataLine = new Vector<>(Arrays.asList(line.split(seperator)));
		loginRegisterRecord.dateTime = loginRegisterDataLine.get(0);
		loginRegisterRecord.userName = loginRegisterDataLine.get(1);
		loginRegisterRecord.password = clsEncryption.decryptText(loginRegisterDataLine.get(2),decryptionKey);
		loginRegisterRecord.permissions = Integer.parseInt(loginRegisterDataLine.get(3));
		return loginRegisterRecord;
	}
	private String _PrepareLoginRecord()
	{
		int encryptionKey=2;
		String seperator = "#//#";
		String line = "";
		line += clsDate.getSystemDateTimeString()+ seperator;
		line += _UserName + seperator;
		line += clsEncryption.encryptText(_Password,encryptionKey) + seperator;
		line += Integer.toString(_Permissions);
		return line;
	}
	private static clsUser _ConvertLineToUserObject(String line) {
	    String separator = "#//#";
	    int decryptionKey = 2;
	    Vector<String> vUserData = new Vector<>(Arrays.asList(line.split(separator)));
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
	private static String _ConvertUserObjectToLine(clsUser user)
	{
		String seperator = "#//#";
		int encryptionKey=2;
		String line = "";
		line += user.getFirstName() + seperator;
		line += user.getLastName() + seperator;
		line += user.getEmail() + seperator;
		line += user.getPhone() + seperator;
		line += user.getUserName() + seperator;
		line += clsEncryption.encryptText(user.getPassword(),encryptionKey) + seperator;
		line += Integer.toString(user.getPermissions() )+ seperator;
		return line;
	}
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
	        e.printStackTrace();
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
	private static clsUser _GetEmptyUserObject()
	{
		return new clsUser(
		        enMode.EmptyMode,
		        "",
		        "",
		        "",
		        "",
		        "",
		        "",
		        0
		    );
	}
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
	private static void _AddDataLineToFile(String stDataLine) {
	    String fileName = "Users.txt";
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) { 
	        bw.write(stDataLine);
	        bw.newLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	private void _AddNew()
	{
		_AddDataLineToFile(_ConvertUserObjectToLine(this));
	}
	public clsUser(enMode mode, String firstName, String lastName,String email, String phone, String userName, String password,int permissions) {
		super(firstName, lastName, email, phone); 
		this._Mode = mode;
		this._UserName = userName;
		this._Password = password;
        this._Permissions = permissions;
}
	public enum enPermissions {
	    eAll(-1),
	    pBooks(1),
	    pAddNewBook(2),
	    pDeleteBook(4),
	    pUpdateBooks(8),
	    pFindBook(16),
	    pManageUsers(32),
	    pShowLoginRegister(64);

	    private final int value;

	    enPermissions(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	
	public static Vector<clsLoginRegisterRecord> getLoginRegisterList() {
	    Vector<clsLoginRegisterRecord> vLoginRegisterRecords = new Vector<>();
	    String fileName = "LoginRegister.txt";

	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            clsLoginRegisterRecord loginRegisterRecord = _ConvertLoginRegisterLineToRecord(line);
	            vLoginRegisterRecords.add(loginRegisterRecord);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return vLoginRegisterRecords;
	}
	
	public boolean isEmpty() {
	    return this._Mode == enMode.EmptyMode;
	}
	public boolean isMarkedForDeleted()
	{
		return _MarkedForDelete;
	}
	public void setUserName(String userName) {
		this._UserName=userName;
	}
	public String getUserName() {
		return this._UserName;
	}
	
	public void setPassword(String password) {
		this._Password=password;
	}
	public String getPassword() {
		return this._Password;
	}
	public void setPermissions(int permissions) {
		this._Permissions=permissions;
	}
	public int getPermissions() {
		return this._Permissions;
	}
	public static clsUser find(String userName) {
	    String fileName = "Users.txt";
	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            clsUser user = _ConvertLineToUserObject(line);
	            if (user.getUserName().equals(userName)) { // use equals() for Strings
	                return user;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return _GetEmptyUserObject();
	}
	
	public  static clsUser find(String userName, String password) {
	    String fileName = "Users.txt";
	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            clsUser user = _ConvertLineToUserObject(line);
	            // Use .equals() to compare String content
	            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
	                return user;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return _GetEmptyUserObject();
	}
	
	public enum enSaveResults {
	    svFaildEmptyObject(0),
	    svSucceeded(1),
	    svFaildUserExists(2);

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
	            _Update();
	            return enSaveResults.svSucceeded;

	        case AddNewMode:
	            if (clsUser.isUserExist(_UserName)) {
	                return enSaveResults.svFaildUserExists;
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

	public static boolean isUserExist(String userName)
	{
		clsUser user = find(userName);
		return !user.isEmpty();
	}
	public static clsUser GetAddNewUserObject(String UserName)
	{
		return new clsUser(enMode.AddNewMode, "", "", "", "", UserName, "", 0);
	}
	public static Vector <clsUser> getUsersList()
	{
		return _LoadUsersDataFromFile();
	}
	public boolean Delete() {
	    Vector<clsUser> vUsers = _LoadUsersDataFromFile();

	    for (clsUser user : vUsers) {
	        if (user.getUserName().equals(this._UserName)) { 
	            user._MarkedForDelete = true;
	            break;
	        }
	    }
	    _SaveUsersDataToFile(vUsers);

	    // Reset current object to empty
	    clsUser emptyUser = _GetEmptyUserObject();
	    this._Mode = emptyUser._Mode;
	    this._UserName = emptyUser._UserName;
	    this._Password = emptyUser._Password;
	    this._Permissions = emptyUser._Permissions;
	    this._MarkedForDelete = emptyUser._MarkedForDelete;
	    return true;
	}
	
	public void registerLogin() {
	    String fileName = "LoginRegister.txt";
	    String dataLine = _PrepareLoginRecord();

	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) { // append mode
	        bw.write(dataLine);
	        bw.newLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean checkAccessPermission(enPermissions permission) {
	    int userPermissions = this.getPermissions(); 
	    if (userPermissions == enPermissions.eAll.getValue()) {
	        return true;
	    }
	    if ((permission.getValue() & userPermissions) == permission.getValue()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
}
