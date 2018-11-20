package it.lsoft.rest.contact;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.Utils;

@Path("/contact")
public class ContactHandler {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response method(@HeaderParam("Authorization") String token,
								   @HeaderParam("Language") String language) 
	{
		Utils ut = new Utils();
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();
	}
}
