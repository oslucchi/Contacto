package it.lsoft.dbUtils;

import java.util.ArrayList;

public class Company extends DBInterface
{
	private static final long serialVersionUID = -4303695469241681652L;

	protected int idCompany;
	protected String name;
	protected boolean isGroup;
	protected int idGroup;
	protected ArrayList<Subsidiary> subsidiaries;
	
	private void setNames()
	{
		tableName = "Company";
		idColName = "idCompany";
	}

	public Company()
	{
		setNames();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Company> listCompanies() throws Exception
	{
		String sql = 
				"SELECT * " + 
				"FROM Company";
		DBConnection conn = null;
		ArrayList<Company> cmpList = null;
		try
		{
			conn = DBInterface.connect();
			cmpList = (ArrayList<Company>) Company.populateCollection(conn, sql, Company.class);
			for (Company company : cmpList) 
			{
				company.subsidiaries = (ArrayList<Subsidiary>) Subsidiary.getByCompanyId(conn, company.getIdCompany());
			}
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return cmpList;
	}

	public static Company getByCompanyId(int idCompany) throws Exception
	{
		String sql = 
				"SELECT * " + 
				"FROM Company " +
				"WHERE idCompany = " + idCompany;
		DBConnection conn = null;
		Company company = null;
		try
		{
			conn = DBInterface.connect();
			company = (Company) Company.populateByQuery(conn, sql, Company.class);
			company.subsidiaries = (ArrayList<Subsidiary>) Subsidiary.getByCompanyId(conn, company.getIdCompany());
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return company;
	}

	public static Company getBySubsidiaryId(int idSubsidiary) throws Exception
	{
		String sql;
		
		DBConnection conn = null;
		Company company = null;
		Subsidiary subsidiary = null;
		try
		{
			conn = DBInterface.connect();
			subsidiary = Subsidiary.getById(conn, idSubsidiary);
			sql = "SELECT * " + 
				  "FROM Company " +
				  "WHERE idCompany = " + subsidiary.idCompany;
			company = (Company) Company.populateByQuery(conn, sql, Company.class);
			company.subsidiaries = new ArrayList<Subsidiary>();
			company.subsidiaries.add(subsidiary);
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return company;
	}


	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public ArrayList<Subsidiary> getSubsidiaries() {
		return subsidiaries;
	}

	
}
