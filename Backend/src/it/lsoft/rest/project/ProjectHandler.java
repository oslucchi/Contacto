
package it.lsoft.rest.project;

import java.util.ArrayList;

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
import it.lsoft.dbUtils.Project;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.Utils;

@Path("/project")
public class ProjectHandler {
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
		ArrayList<Project> prjList = null;
		Project prjObject = new Project();
		Utils ut = new Utils();
		try
		{
			prjList = prjObject.listProjects();
			if (prjList == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			else
			{
				ut.addToJsonContainer("projects", prjList, true);
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
