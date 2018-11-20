package it.lsoft.dbUtils;

import java.util.ArrayList;

public class Subsidiary extends DBInterface
{	
	private static final long serialVersionUID = -4303695469241681652L;

	protected int idSubsidiary;
	protected int idCompany;
	protected String name;
	protected String addressLine1;
	protected String addressLine2;
	protected String zipCode;
	protected String city;
	protected String provinceOrState;
	protected String country;
	protected String idExternalERP;
	protected String phone1;
	protected String phone2;
	protected String email;
	protected String web;
	
	private void setNames()
	{
		tableName = "Subsidiary";
		idColName = "idSubsidiary";
	}

	public Subsidiary()
	{
		setNames();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Subsidiary> getByCompanyId(DBConnection conn, int idCompany) throws Exception
	{
		ArrayList<Subsidiary> subsidiaries;
		String sql = 
				"SELECT * " +
				"FROM Subsidiary " +
				"WHERE idCompany = " + idCompany;
		try
		{
			subsidiaries = (ArrayList<Subsidiary>) Subsidiary.populateCollection(conn, sql, Subsidiary.class);
		}
		catch(Exception e)
		{
			throw(e);
		}
		return subsidiaries;
	}
	
	public static Subsidiary getById(DBConnection conn, int idSubsidiary) throws Exception
	{
		Subsidiary subsidiariy;
		String sql = 
				"SELECT * " +
				"FROM Subsidiary " +
				"WHERE idSubsidiary = " + idSubsidiary;
		try
		{
			subsidiariy = (Subsidiary) Subsidiary.populateByQuery(conn, sql, Subsidiary.class);
		}
		catch(Exception e)
		{
			throw(e);
		}
		return subsidiariy;
	}

	public int getIdSubsidiary() {
		return idSubsidiary;
	}

	public void setIdSubsidiary(int idSubsidiary) {
		this.idSubsidiary = idSubsidiary;
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

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvinceOrState() {
		return provinceOrState;
	}

	public void setProvinceOrState(String provinceOrState) {
		this.provinceOrState = provinceOrState;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIdExternalERP() {
		return idExternalERP;
	}

	public void setIdExternalERP(String idExternalERP) {
		this.idExternalERP = idExternalERP;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}
}
