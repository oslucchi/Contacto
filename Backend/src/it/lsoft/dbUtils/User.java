package it.lsoft.dbUtils;


public class User extends DBInterface
{
	private static final long serialVersionUID = -851756304221253759L;

	protected int idUser;
	protected int idRole;
	protected String firstName;
	protected String lastName;
	protected String phoneMob;
	protected String phoneLand;
	protected String email;

	private void setNames()
	{
		tableName = "User";
		idColName = "idUser";
	}

	public User()
	{
		setNames();
	}

	public User(DBConnection conn, int id) throws Exception
	{
		getUser(conn, id);
	}

	public User(DBConnection conn, String email) throws Exception
	{
		getUser(conn, email);
	}

	public void getUser(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public void getUser(DBConnection conn, String email) throws Exception
	{
		setNames();
		findByEmail(conn, email);
	}

	public void findByEmail(DBConnection conn, String email) throws Exception
	{
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE email = '" + email + "'";
		this.populateObject(conn, sql, this);
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
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

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getPhoneLand() {
		return phoneLand;
	}

	public void setPhoneLand(String phoneLand) {
		this.phoneLand = phoneLand;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
