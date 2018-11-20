package it.lsoft.rest.menu;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.Menu;
import it.lsoft.dbUtils.User;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.SessionData;
import it.lsoft.rest.Utils;

@Path("/menu")
public class MenuHandler {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMenu(@HeaderParam("Authorization") String token,
							@HeaderParam("Language") String language) 
	{
		Utils ut = new Utils();
		try
		{
			SessionData sd = SessionData.getInstance();
			User u = sd.getBasicProfile(token);
			if (u == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "auth.sessionExpired"), true);
			}
			else
			{
				Menu menu = new Menu(language, u.getIdRole());
				Menu a = menu.getMenu(u.getIdRole(), language);
				ut.addToJsonContainer("menu", a, true);
			}
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "auth.sessionExpired"), true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();
	}
}
