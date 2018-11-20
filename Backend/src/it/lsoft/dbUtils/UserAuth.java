package it.lsoft.dbUtils;

import java.util.Date;

public class UserAuth extends DBInterface
{
	private static final long serialVersionUID = 7371371453325793193L;

	protected int idUserAuth;
	protected int idUser;
	protected Date createdOn;
	protected Date lastRefreshedOn;
	protected String lastActiveToken;

	private void setNames()
	{
		tableName = "UserAuth";
		idColName = "idUserAuth";
	}

	public UserAuth()
	{
		setNames();
	}

	public UserAuth(DBConnection conn, int id) throws Exception
	{
		getUserAuth(conn, id);
	}

	public void getUserAuth(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static UserAuth findToken(DBConnection conn, String token)
	{
		String sql = "SELECT * " +
				 	 "FROM UserAuth " +
				 	 "WHERE lastActiveToken = '" + token + "'";
		UserAuth ua = null;
		try
		{
			ua = (UserAuth) UserAuth.populateByQuery(conn, sql, UserAuth.class);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				return null;
			}
		}
		return(ua);
	}
	
	public static UserAuth findUserId(DBConnection conn, int userId)
	{
		String sql = "SELECT * " +
				 	 "FROM UserAuth " +
				 	 "WHERE idUser = " + userId;
		UserAuth ua = null;
		try
		{
			ua = (UserAuth) UserAuth.populateByQuery(conn, sql, UserAuth.class);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				return null;
			}
		}
		return(ua);
	}

	public int getIdUserAuth() {
		return idUserAuth;
	}

	public void setIdUserAuth(int idUserAuth) {
		this.idUserAuth = idUserAuth;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastRefreshedOn() {
		return lastRefreshedOn;
	}

	public void setLastRefreshedOn(Date lastRefreshedOn) {
		this.lastRefreshedOn = lastRefreshedOn;
	}

	public String getLastActiveToken() {
		return lastActiveToken;
	}

	public void setLastActiveToken(String lastActiveToken) {
		this.lastActiveToken = lastActiveToken;
	}
}
