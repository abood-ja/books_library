package aboodZaidLibrary;

public class clsPerson {
	private  String _FirstName;
	private  String _LastName;
	private  String _Email;
	private  String _Phone;
	public clsPerson(String firstName, String lastName, String email, String phone) {
	    this._FirstName = firstName;
	    this._LastName = lastName;
	    this._Email = email;
	    this._Phone = phone;
	}
	public void setFirstName(String firstName) {
		this._FirstName=firstName;
	}
	public void setLastName(String lastName) {
		this._LastName=lastName;
	}
	public void setEmail(String email) {
		this._Email=email;
	}
	public void setPhone(String phone) {
		this._Phone=phone;
	}
	public String getFirstName() {
		return this._FirstName;
	}
	public String getLastName() {
		return this._LastName;
	}
	public String getEmail() {
		return this._Email;
	}
	public String getPhone() {
		return this._Phone;
	}
	public String getFullName() {
		return this._FirstName +" " +this._LastName;
	}
}
