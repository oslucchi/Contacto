package it.lsoft.dbUtils;

public class PasswordHandler extends DBInterface {
	private static final long serialVersionUID = -307149576390277583L;
	
	protected int idUser;
	protected String password;

	private void setNames()
	{
		tableName = "User";
		idColName = "idUser";
	}

	public PasswordHandler() throws Exception
	{
		setNames();
	}

	public String userPassword(DBConnection conn, int idUsers) 
	{
		setNames();
		String sql = "SELECT password FROM User WHERE idUser = " + idUsers;
		try 
		{
			populateObject(conn, sql, this);
		}
		catch(Exception e) 
		{
			return null;
		}
		return password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void updatePassword(DBConnection conn, boolean inTransaction) throws Exception
	{
		String sql = "UPDATE User SET  password = '" + password + "' WHERE idUser = " + idUser;
		executeStatement(conn, sql, inTransaction);
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	
}
