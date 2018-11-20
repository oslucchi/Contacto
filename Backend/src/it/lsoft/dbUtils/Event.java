package it.lsoft.dbUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event extends DBInterface
{
	public final static int NO_COMPANY = 0;

	private static final long serialVersionUID = 1977368266520186491L;
	
	protected int idEvent;
	protected int idOwner;
	protected int idCompany;
	protected int actionId;
	protected Date calendarDate;
	protected int alertType;
	protected String companyName;

	private void setNames()
	{
		tableName = "Event";
		idColName = "idEvent";
	}

	public Event()
	{
		setNames();
	}

	@SuppressWarnings("unchecked")
	public static  ArrayList<Event> listEvents(int userId, Date fromDate, int idCompany) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = 
				"SELECT e.*, c.name AS companyName " + 
				"FROM Event e INNER JOIN Company c ON (c.idCompany = e.idCompany) " +
				"WHERE idOwner = " + userId + " " +
				   (fromDate != null ? " AND calendarDate >= '" + format.format(fromDate) + "'" : "") +
				   (idCompany != 0 ? " AND idCompany = " + idCompany: "");
		DBConnection conn = null;
		ArrayList<Event> eventList;
		try
		{
			conn = DBInterface.connect();
			eventList = (ArrayList<Event>) Event.populateCollection(conn, sql, Event.class);
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return eventList;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public int getIdOwner() {
		return idOwner;
	}

	public void setIdOwner(int idOwner) {
		this.idOwner = idOwner;
	}

	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public Date getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public int getAlertType() {
		return alertType;
	}

	public void setAlertType(int alertType) {
		this.alertType = alertType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
