package it.lsoft.rest.login;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.owlike.genson.Genson;

import it.lsoft.dbUtils.DBConnection;
import it.lsoft.dbUtils.DBInterface;
import it.lsoft.dbUtils.PasswordHandler;
import it.lsoft.dbUtils.Roles;
import it.lsoft.dbUtils.User;
import it.lsoft.dbUtils.UserAuth;
import it.lsoft.rest.ApplicationProperties;
import it.lsoft.rest.Constants;
import it.lsoft.rest.LanguageResources;
import it.lsoft.rest.SessionData;
import it.lsoft.rest.Utils;
import it.lsoft.rest.jsonInt.AuthJson;
import it.lsoft.rest.jsonInt.UserJson;

@Path("/auth")
public class Auth {
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());

	private Genson genson = new Genson();
	private User u = null;

	@Context
	private HttpServletRequest servletRequest;

	protected Response populateUsersTable(UserJson jsonIn, boolean accessByExternalAuth, String language)
	{
		DBConnection conn = null;
		try 
		{
			conn = DBInterface.TransactionStart();
			u = new User();
			u.setEmail(jsonIn.email);
			u.setFirstName(jsonIn.firstName);
			u.setLastName(jsonIn.lastName);
			u.setIdRole(Roles.USER);
			u.setPhoneLand(jsonIn.phoneLand);
			u.setPhoneMob(jsonIn.phoneMob);
			u.setIdUser(u.insertAndReturnId(conn, "idUser", u));
			
			PasswordHandler pw = new PasswordHandler();
			if (jsonIn.password != null)
			{
				pw.setIdUser(u.getIdUser());
				pw.setPassword(jsonIn.password);
				pw.updatePassword(conn, false);
			}
			DBInterface.TransactionCommit(conn);
		}
		catch(Exception e) 
		{
			DBInterface.TransactionRollback(conn);
			if (e.getCause().getMessage().substring(0, 15).compareTo("Duplicate entry") == 0)
			{
				if (!accessByExternalAuth)
					return Utils.jsonizeResponse(Response.Status.FORBIDDEN, null, language, "users.alreadyRegistered");
			}
			else
			{
				log.warn("Exception " + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.FORBIDDEN, e, language, "generic.execError");
			}
		}
		finally
		{
			DBInterface.disconnect(conn);
		}

		return null;
	}

	protected Response populateUserAuthTable(String token, int userId, String language)
	{
		DBConnection conn = null;
		try
		{
			conn = DBInterface.connect();
		}
		catch(Exception e)
		{
			log.warn("Exception " + e.getMessage(), e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, language, "generic.execError");
		}
		return(populateUsersAuthTable(conn, token, userId, language));
	}
	
	protected Response populateUsersAuthTable(DBConnection conn, String token, int userId, String language)
	{
		UserAuth ua = null;
		try 
		{
			log.debug("Looking up the user '" + userId + "' to update or insert his UserAuth record");
			ua = UserAuth.findUserId(conn, userId);
			if (ua != null)
			{
				log.debug("Refresh the entry with new data");
				ua.setLastRefreshedOn(new Date());
				ua.setLastActiveToken(token);
				ua.setIdUser(userId);
				ua.update(conn, "idUserAuth");
			}
			else
			{
				log.debug("The user was not found, create a new entry in the table");
				ua = new UserAuth();
				ua.setIdUser(userId);
				ua.setLastActiveToken(token);
				ua.setCreatedOn(new Date());
				ua.setLastRefreshedOn(ua.getCreatedOn());
				ua.setIdUserAuth(ua.insertAndReturnId(conn, "idUserAuth", ua));
			}
		}
		catch(Exception e) 
		{
			log.warn("Exception " + e.getMessage(), e);
			return Utils.jsonizeResponse(Response.Status.FORBIDDEN, e, language, "generic.execError");
		}

		// Updating the session data. Any user should have maximum one entry in SD
		SessionData sa = SessionData.getInstance();
		Object[] userProfile = sa.getSessionData(userId);
		if (userProfile == null)
		{
			userProfile = new Object[SessionData.SESSION_ELEMENTS];
			userProfile[SessionData.LANGUAGE] = new Integer(Utils.setLanguageId(language));
			userProfile[SessionData.CLIENT_IP] = servletRequest.getRemoteAddr();
		}
		try 
		{
			userProfile[SessionData.BASIC_PROFILE] = (u == null ? new User(conn, userId) : u);
		}
		catch(Exception e) 
		{
			;
		}
		
		sa.updateSession(userId, userProfile, token);
		return null;		
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(UserJson jsonIn, @HeaderParam("Language") String language) 
	{
		int languageId = Utils.setLanguageId(language);
		if (jsonIn.email == null)
		{
			return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "users.badMail");
		}
		if ((jsonIn.password == null) || (jsonIn.password.length() == 0))
		{
			return Utils.jsonizeResponse(Response.Status.FORBIDDEN, null, language, "users.badPassword");
		}

		Response response;
		if ((response = populateUsersTable(jsonIn, false, language)) != null)
			return response;

		Utils ut = new Utils();
		ut.addToJsonContainer("responseMessage", LanguageResources.getResource(languageId, "auth.registerRedirectMsg"), true);
		return Response.status(Response.Status.OK).entity(ut.jsonize()).build();
	}

	@GET
	@Path("/isAuthenticated")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isAtuhenticated(@HeaderParam("Language") String language,
									@HeaderParam("Authorization") String token) 
	{
		SessionData sa = SessionData.getInstance();
		Utils ut = new Utils();
		if (sa.getBasicProfile(token) != null)
		{
			ut.addToJsonContainer("authorized", "true", true);
			return Response.status(Status.OK).entity(ut.jsonize()).build();
		}
		
		if (UserAuth.findToken(null, token) == null)
		{
			ut.addToJsonContainer("authorized", "false", true);
			return Response.status(Status.UNAUTHORIZED).entity(ut.jsonize()).build();
		}
		ut.addToJsonContainer("authorized", "true", true);
		return Response.status(Status.OK).entity(ut.jsonize()).build();
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(AuthJson jsonIn, 
						  @HeaderParam("Language") String language,
						  @QueryParam("fromState") String fromState) 
	{ 
		String email = jsonIn.email; 
		String password = jsonIn.password; 
		User u;

		/*
		 * A new login always requires a new token to be generated
		 */
		String token = UUID.randomUUID().toString();
		log.debug("Login called for user '" + email + "'");
		DBConnection conn = null;
		try 
		{
			conn = DBInterface.connect();
			u = new User();
			log.debug("Get user by email");
			u.findByEmail(conn, email);

			PasswordHandler pw = new PasswordHandler();
			pw.userPassword(conn, u.getIdUser());
			
			log.debug("Found. Password in database is '" + pw.getPassword() + "'");
			if ((pw.getPassword() == null) || (pw.getPassword().compareTo(password) != 0))
			{
				log.debug("Wrong password, returning UNAUTHORIZED");
				return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "auth.wrongCredentials");
			}
			populateUsersAuthTable(conn, token, u.getIdUser(), language);
		}
		catch(Exception e)
		{
			DBInterface.disconnect(conn);
			if (e.getMessage().compareTo("No record found") == 0)
			{
				log.debug("Email not found, returning UNAUTHORIZED");
				return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "auth.mailNotRegistered");
			}
			else
			{
				log.debug("Generic error " + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "generic.execError");
			}
		}

		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("token", token);
		jsonResponse.put("user", u);
		jsonResponse.put("toState", fromState);
		
		String entity = genson.serialize(jsonResponse);
		return Response.status(Response.Status.OK).entity(entity).build();
	}
	
	@POST
	@Path("/loginByToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginByToken(@HeaderParam("Authorization") String token, 
								 @HeaderParam("Language") String language)
	{
		UserAuth ua = null;
		SessionData sa = SessionData.getInstance();

		DBConnection conn = null;
		try 
		{
			conn = DBInterface.connect();
			if ((ua = UserAuth.findToken(conn, token)) == null)
			{
				sa.removeUser(token);
				DBInterface.disconnect(conn);
				return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "auth.sessionExpired");
			}
			if (prop.getSessionExpireTime() != 0)
			{
				if (ua.getLastRefreshedOn().getTime() + prop.getSessionExpireTime() * 1000 < new Date().getTime())
				{
					ua.delete(conn, ua.getIdUserAuth());
					sa.removeUser(token);
					DBInterface.disconnect(conn);
					return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, null, language, "auth.sessionExpired");
				}
			}
		}
		catch(Exception e) 
		{
			log.warn("Exception " + e.getMessage(), e);
			sa.removeUser(token);
			DBInterface.disconnect(conn);
			return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, e, language, "auth.sessionExpired");
		}

		Object[] userProfile = sa.getWholeProfile(token);
		try
		{
			ua.setLastRefreshedOn(new Date());
			ua.update(conn, "idUserAuth");
			if (userProfile == null)
			{
				sa.addUser(conn, ua.getIdUser(), Constants.getLanguageCode(language), servletRequest);
				userProfile = new Object[SessionData.SESSION_ELEMENTS];
				userProfile = sa.getSessionData(ua.getLastActiveToken());
			}
			else
			{
				userProfile[SessionData.BASIC_PROFILE] = new User(conn, ua.getIdUser());
				userProfile[SessionData.LANGUAGE] = Constants.getLanguageCode(language);
				userProfile[SessionData.CLIENT_IP] = servletRequest.getRemoteAddr();
				sa.updateSession(token, userProfile);
			}
		}
		catch(Exception e) 
		{
			log.error("Exception " + e.getMessage() + " setting up sessionData", e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		
		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("token", token);
		jsonResponse.put("user", userProfile[0]);
		String entity = genson.serialize(jsonResponse);
		return Response.status(Response.Status.OK).entity(entity)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
//				.allow("OPTIONS")
				.build();
	}
    
//	@POST
//	@Path("/changePassword")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response changePassword(AuthJson jsonIn, 
//								   @HeaderParam("Language") String language)
//	{		
//		jsonIn.token = UUID.randomUUID().toString();		
//		if (jsonIn.username == null)
//		{
//			return Utils.jsonizeResponse(Response.Status.FORBIDDEN, null, language, "users.badMail");
//		}
//		DBConnection conn = null;
//		try 
//		{
//			conn = DBInterface.connect();
//		}
//		catch(Exception e)
//		{
//			DBInterface.disconnect(conn);
//			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, language, "generic.execError");
//		}
//		
//		RegistrationConfirm rc = null;
//		try
//		{
//			u = new Users(conn, jsonIn.username);
//			rc = new RegistrationConfirm();
//			if (rc.findActiveRecordByUserId(conn, u.getIdUsers()) == null)
//			{
//				log.trace("No existing request for user " + jsonIn.username + 
//						  " generating a new token '" + token + "'");
//				rc = null;
//
//			}
//			else
//			{
//				log.trace("An existing request for user " + rc.getUserId() + 
//						  " is already there on token '" + rc.getToken() + "'");
//				jsonIn.token = rc.getToken();
//			}
//		}
//		catch(Exception e)
//		{
//			DBInterface.disconnect(conn);
//			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, language, "generic.execError");
//		}
//
//		try
//		{
//			if (rc == null)
//			{
//				rc = new RegistrationConfirm();
//				rc.setCreated(new Date());
//				rc.setStatus(Constants.STATUS_ACTIVE);
//				rc.setToken(jsonIn.token);
//				rc.setUserId(u.getIdUsers());
//				rc.setPasswordChange(jsonIn.password);
//				rc.insert(conn, "idRegistrationConfirm", rc);
//			}
//			else
//			{
//				jsonIn.token = rc.getToken();
//				rc.setPasswordChange(jsonIn.password);
//				rc.update(conn, "idRegistrationConfirm");
//			}
//		}
//		catch(Exception e) 
//		{
//			log.warn("Exception " + e.getMessage(), e);
//			return Utils.jsonizeResponse(Response.Status.FORBIDDEN, e, language, "users.badMail");
//		}
//		finally
//		{
//			DBInterface.disconnect(conn);
//		}
//
//		Response response;
//		if ((response = prepareAndSendMail("mail.passwordChange", "mail.passwordChangeSubject", 
//									  "confirmPasswordChange", language, jsonIn)) != null)
//		{
//			return response;
//		}
//		log.trace("Confirmation mail sent to: " + jsonIn.username);
//		return Response.status(Response.Status.OK)
//				.entity(ResponseEntityCreator.formatEntity(language, "auth.registerRedirectMsg")).build();
//	}
	
//	@GET
//	@Path("confirmPasswordChange/{token}")
//	@Consumes(MediaType.TEXT_HTML)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response confirmPasswordChange(@PathParam("token") String token) 
//	{
//		String language = prop.getDefaultLang();
//		URI location = null;
//		String uri = prop.getWebHost() + "/" + prop.getRedirectHome() + "?token=" + token;
//
//		RegistrationConfirm rc = null;
//		DBConnection conn = null;
//		try 
//		{
//			conn = DBInterface.TransactionStart();
//			rc = new RegistrationConfirm();
//			rc.findActiveRecordByToken(conn, token);
//			PasswordHandler pw = new PasswordHandler();
//			pw.setPassword(rc.getPasswordChange());
//			pw.setIdUsers(rc.getUserId());
//			pw.updatePassword(conn, true);
//			populateUsersAuthTable(conn, token, rc.getUserId(), language);
//		}
//		catch(Exception e) 
//		{
//			log.warn("Exception " + e.getMessage(), e);
//			DBInterface.TransactionRollback(conn);
//			DBInterface.disconnect(conn);
//			return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, e, language, "auth.confirmTokenInvalid");
//		}
//		
//		try 
//		{
//			rc.setStatus(Constants.STATUS_COMPLETED);
//			rc.update(conn, "idRegistrationConfirm");
//			DBInterface.TransactionCommit(conn);
//		}
//		catch(Exception e)
//		{
//			log.warn("Exception " + e.getMessage(), e);
//			DBInterface.TransactionRollback(conn);
//			log.error("Exception updating the registration confirm record. (" + e.getMessage() + ")");
//		}
//		finally
//		{
//			DBInterface.disconnect(conn);
//		}
//
//		
//		try 
//		{
//			location = new URI(uri);
//			return Response.seeOther(location).build();
//		}
//		catch(URISyntaxException e) 
//		{
//			log.error("Invalid URL generated '" + uri + "'. Error " + e.getMessage(), e);
//		}
//		catch(Exception e) {
//			log.error("Exception " + e.getMessage() + " updating user and registration token", e);
//		}
//		return Response.status(Response.Status.OK)
//				.entity("").build();
//	}


	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(@HeaderParam("Authorization") String token, @HeaderParam("Language") String language)
	{
		DBConnection conn = null;
		try 
		{
			conn = DBInterface.connect();
			UserAuth ua = UserAuth.findToken(conn, token);
			if (ua != null)
				ua.delete(conn, ua.getIdUserAuth());
		}
		catch(Exception e) 
		{
			log.warn("Exception " + e.getMessage(), e);
			return Utils.jsonizeResponse(Response.Status.UNAUTHORIZED, e, language, "auth.tokenNotFound");
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		
		SessionData.getInstance().removeUser(token);
		return Response.status(Response.Status.OK)
				.entity("{}")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.build();
	}
	
	public User getUser()
	{
		return u;
	}
}
