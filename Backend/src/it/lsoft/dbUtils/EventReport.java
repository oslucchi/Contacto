package it.lsoft.dbUtils;

public class EventReport extends DBInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1977368266520186491L;
	
	protected int idEventReport;
	protected int idEvent;
	protected String report;
	protected String text;

	private void setNames()
	{
		tableName = "EventReport";
		idColName = "idEventReport";
	}

	public EventReport()
	{
		setNames();
	}

	public static  EventReport getEventReport(int idEventReport) throws Exception
	{
		String sql = 
				"SELECT * " + 
				"FROM EventReport " +
				"WHERE idEventReport = " + idEventReport;
		
		DBConnection conn = null;
		EventReport eventReport;
		try
		{
			conn = DBInterface.connect();
			eventReport = (EventReport) EventReport.populateByQuery(conn, sql, EventReport.class);
		}
		catch(Exception e)
		{
			throw(e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return eventReport;
	}

	public int getIdEventReport() {
		return idEventReport;
	}

	public void setIdEventReport(int idEventReport) {
		this.idEventReport = idEventReport;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
