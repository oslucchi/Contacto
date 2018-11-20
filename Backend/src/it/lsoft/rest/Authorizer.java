package it.lsoft.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.DBConnection;
import it.lsoft.dbUtils.DBInterface;
import it.lsoft.dbUtils.UserAuth;

@Provider
@PreMatching
public class Authorizer implements ContainerRequestFilter 
{
	ApplicationProperties prop = ApplicationProperties.getInstance();
	private final Logger log = Logger.getLogger(this.getClass());
	
	private static String[] authorizedList = null;
	private static String[] authorizedByRootList = null;
	
	@Context
	private HttpServletRequest servletRequest;
	
	@Override
	public void filter(ContainerRequestContext request) throws IOException 
	{
		String path = request.getUriInfo().getPath();
		
		if (authorizedByRootList == null)
		{
			authorizedByRootList = prop.getNoAuthorizationRequiredRoot().split(",");
		}
		for(String authorized : authorizedByRootList)
		{
			if (path.startsWith(authorized))
			{
				log.trace("Publicly avalable request under authorized root (" + path + "). It is authorized");
				return;
			}
		}
		
		if (authorizedList == null)
		{
			authorizedList = prop.getNoAuthorizationRequired().split(",");
		}
		if (request.getHeaderString("Edit-Mode") == null)
		{
			for(String authorized : authorizedList)
			{
				if (path.compareTo(authorized) == 0)
				{
					log.trace("Publicly avalable request (" + path + "). It is authorized");
					return;
				}
			}
		}
		
		String token = request.getHeaderString("Authorization");
		String language = request.getHeaderString("Language");
		if (language == null)
		{
			language = prop.getDefaultLang();
		}

		SessionData sd = SessionData.getInstance();
		Object[] sessionProfile = sd.getSessionData(token);
		UserAuth ua = null;
		Utils ut = new Utils();
		if (sessionProfile != null)
		{
			if (((String)sessionProfile[SessionData.CLIENT_IP]).compareTo(servletRequest.getRemoteAddr()) != 0)
			{
				log.trace("The token '" + token + "' is valid for this session but the IP changed. " +
						  "Rejected the request and invalidate the token");
				ut.addToJsonContainer("authorized", "false", true);
				request.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.entity(ut.jsonize())
						.build());;
				sd.removeUser(token);
				if ((ua = UserAuth.findToken(null, token)) != null)
				{
					DBConnection conn = null;
					try 
					{
						conn = DBInterface.connect();
						ua.delete(conn, ua.getIdUserAuth());
					}
					catch(Exception e)
					{
						log.error("Can't delete invalid token (change of IP) from UserAuth"); 
					}
					finally
					{
						DBInterface.disconnect(conn);
					}
				}
			}
			else
			{
				if (sd.getLanguage(token) != Constants.getLanguageCode(language))
				{
					sd.setLanguage(token, Constants.getLanguageCode(language));
				}
				log.trace("Recognized valid token '" + token + "' for this session. language " + language + ". Request authorized");
			}
			return;
		}
		if ((ua = UserAuth.findToken(null, token)) == null)
		{
			log.warn("Token '" + token + "' not found. Returning anauthorized");
			ut.addToJsonContainer("authorized", "false", true);
			request.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity(ut.jsonize())
					.build());;
			return;
		} 
		try 
		{
			log.trace("Adding user to session data");
			sd.addUser(ua, language, servletRequest);
		}
		catch(Exception e) 
		{
			log.warn("Exception '" + e.getMessage() + "' adding user to session data. Returning anauthorized");
			ut.addToJsonContainer("authorized", "false", true);
			request.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity(ut.jsonize())
					.build());;
		}
		log.trace("Request on '" + path + "' language " + language + " authorized");
		return;
	}

}
