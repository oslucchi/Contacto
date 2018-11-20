package it.lsoft.dbUtils;

public class Contact extends DBInterface
{	
	private static final long serialVersionUID = -4303695469241681652L;

	protected int idContact;
	protected int idSubsidiary;
	protected String firstName;
	protected String lastName;
	protected int idRole;
	protected String phoneMob;
	protected String phoneDesk;
	protected String email;
	
	private void setNames()
	{
		tableName = "Contact";
		idColName = "idContact";
	}

	public Contact()
	{
		setNames();
	}

	public Contact(DBConnection conn, int id) throws Exception
	{
		getContact(conn, id);
	}

	public void getContact(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public int getIdContact() {
		return idContact;
	}

	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}

	public int getIdSubsidiary() {
		return idSubsidiary;
	}

	public void setIdSubsidiary(int idSubsidiary) {
		this.idSubsidiary = idSubsidiary;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getPhoneDesk() {
		return phoneDesk;
	}

	public void setPhoneDesk(String phoneDesk) {
		this.phoneDesk = phoneDesk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
