
package it.lsoft.rest;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.DBConnection;
import it.lsoft.dbUtils.DBInterface;
import it.lsoft.dbUtils.Event;
import it.lsoft.dbUtils.Project;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.Utils;

@Path("/calendar")
public class CalendarHandler {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getProjectList(@HeaderParam("Authorization") String token,
								   @HeaderParam("Language") String language) 
	{
		ArrayList<Event> eventList = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date fromDate = new Date(0);
		Utils ut = new Utils();
		int userId = 2;
		try
		{
			eventList = Event.listEvents(userId, fromDate, Event.NO_COMPANY);
			if (eventList == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			else
			{
				ut.addToJsonContainer("projects", eventList, true);
			}
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError") + 
					"(" + e.getMessage() + ")", true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Project prj,
						@HeaderParam("Authorization") String token,
						@HeaderParam("Language") String language) 
	{
		Project prjObject = new Project();
		DBConnection conn = null;
		try {
			conn = new DBConnection();
			prjObject.insert(conn, prjObject.getIdColName(), prj);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return Response.status(Response.Status.OK).entity("").build();
	}

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updates(Project prj,
						@HeaderParam("Authorization") String token,
						@HeaderParam("Language") String language) 
	{
		Project prjObject = new Project();
		DBConnection conn = null;
		try {
			conn = new DBConnection();
			prj.update(conn, prjObject.getIdColName());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		return Response.status(Response.Status.OK).entity("").build();
	}
}
