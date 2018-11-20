package it.lsoft.dbUtils;

import java.util.ArrayList;
import java.util.Date;

public class Project extends DBInterface
{
	private static final long serialVersionUID = -8397945617320874153L;
	
	protected int idProject;
	protected String name;
	protected int financialEstimation;
	protected String city;
	protected int areaOfInterest;
	protected int idReferral;
	protected String shortDescription;
	protected int idAssignee;
	protected String extReferenceId;
	protected int status;
	protected Date workStartDate;
	protected Date scheduledEnd;
	protected int customerId;
	protected int supervisorId;
	protected int plannerId;
	protected String keywords;
	
	private void setNames()
	{
		tableName = "Project";
		idColName = "idProject";
	}

	public Project()
	{
		setNames();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Project> listProjects() throws Exception
	{
		String sql = 
				"SELECT * " + 
				"FROM Project";
		DBConnection conn = null;
		try
		{
			conn = DBInterface.connect();
			return (ArrayList<Project>) Project.populateCollection(conn, sql, Project.class);
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}

	}
	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFinancialEstimation() {
		return financialEstimation;
	}

	public void setFinancialEstimation(int financialEstimation) {
		this.financialEstimation = financialEstimation;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getAreaOfInterest() {
		return areaOfInterest;
	}

	public void setAreaOfInterest(int areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	public int getIdReferral() {
		return idReferral;
	}

	public void setIdReferral(int idReferral) {
		this.idReferral = idReferral;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public int getIdAssignee() {
		return idAssignee;
	}

	public void setIdAssignee(int idAssignee) {
		this.idAssignee = idAssignee;
	}

	public String getExtReferenceId() {
		return extReferenceId;
	}

	public void setExtReferenceId(String extReferenceId) {
		this.extReferenceId = extReferenceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getWorkStartDate() {
		return (workStartDate == null ? new Date() : workStartDate);
	}

	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}

	public Date getScheduledEnd() {
		return (scheduledEnd == null ? new Date() : scheduledEnd);
	}

	public void setScheduledEnd(Date scheduledEnd) {
		this.scheduledEnd = scheduledEnd;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}

	public int getPlannerId() {
		return plannerId;
	}

	public void setPlannerId(int plannerId) {
		this.plannerId = plannerId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
}
