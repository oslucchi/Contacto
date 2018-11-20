package it.lsoft.rest.event;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.Event;
import it.lsoft.dbUtils.EventReport;
import it.lsoft.dbUtils.User;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.SessionData;
import it.lsoft.rest.Utils;

@Path("/event")
public class EventHandler {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEventList(@HeaderParam("Authorization") String token,
								 @HeaderParam("Language") String language) 
	{
		ArrayList<Event> eventList = null;
		Utils ut = new Utils();
		try
		{
			SessionData sd = SessionData.getInstance();
			User user = sd.getBasicProfile(token);
			if (user == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			eventList = Event.listEvents(user.getIdUser(), null, 0);
			if (eventList == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			else
			{
				ut.addToJsonContainer("events", eventList, true);
			}
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError") + 
					"(" + e.getMessage() + ")", true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();	
	}

	@GET
	@Path("/report/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getReport(@HeaderParam("Authorization") String token,
						      @HeaderParam("Language") String language,
						      @PathParam("id") int idEventReport) 
	{
		EventReport report = null;
		Utils ut = new Utils();
		try
		{
			report = EventReport.getEventReport(idEventReport);
			if (report == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			else
			{
				ut.addToJsonContainer("events", report, true);
			}
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError") + 
					"(" + e.getMessage() + ")", true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();	
	}
}
