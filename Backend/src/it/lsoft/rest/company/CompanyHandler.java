package it.lsoft.rest.company;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.Company;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.Utils;

@Path("/company")
public class CompanyHandler {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCompanyList(@HeaderParam("Authorization") String token,
								   @HeaderParam("Language") String language) 
	{
		ArrayList<Company> cmpList = null;
		Company cmpObject = new Company();
		Utils ut = new Utils();
		try
		{
			cmpList = cmpObject.listCompanies();
			if (cmpList == null)
			{
				ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError"), true);
			}
			else
			{
				ut.addToJsonContainer("companies", cmpList, true);
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
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCompany(
						@HeaderParam("Authorization") String token,
						@HeaderParam("Language") String language,
						@PathParam("id") int idCompany) 
	{
		Company cmpObject = null;
		Utils ut = new Utils();
		try
		{
			cmpObject = Company.getByCompanyId(idCompany);
			ut.addToJsonContainer("company", cmpObject, true);
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError") + 
					"(" + e.getMessage() + ")", true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();	
	}

	@GET
	@Path("/subsidiary/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCompanyBySubsidiaryId(
						@HeaderParam("Authorization") String token,
						@HeaderParam("Language") String language,
						@PathParam("id") int idSubsidiary)
	{
		Company cmpObject = null;
		Utils ut = new Utils();
		try
		{
			cmpObject = Company.getBySubsidiaryId(idSubsidiary);
			ut.addToJsonContainer("company", cmpObject, true);
		}
		catch(Exception e)
		{
			ut.addToJsonContainer("responseMessage", LanguageResources.getResource(language, "generic.execError") + 
					"(" + e.getMessage() + ")", true);
		}
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();	
	}
}
