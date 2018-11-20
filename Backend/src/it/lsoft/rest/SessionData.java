package it.lsoft.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import it.lsoft.dbUtils.DBConnection;
import it.lsoft.dbUtils.DBInterface;
import it.lsoft.dbUtils.User;
import it.lsoft.dbUtils.UserAuth;

public class SessionData {
	final static Logger log = Logger.getLogger(SessionData.class);
	
	final public static int BASIC_PROFILE = 0; // a users Object only
	final public static int WHOLE_PROFILE = 1; // Users and AddressInfo array
	final public static int LANGUAGE = 2; // Language 
	final public static int	CLIENT_IP = 3; // IP the client connects from
	final public static int SESSION_ELEMENTS = 4;
	
	private static Map<String, Object[]> sessionData;
	private static SessionData singletonInstance = null;

	private SessionData()
	{
		sessionData = new HashMap<>();
	}
	
	public static SessionData getInstance()
	{
		if (singletonInstance == null)
		{
			singletonInstance = new SessionData();
		}
		return(singletonInstance);
	}
	
	public User getBasicProfile(String token)
	{
		Object[] profile = sessionData.get(token);
		if (profile == null)
		{
			return(null);
		}
		return((User) profile[BASIC_PROFILE]);
	}

	public Object[] getWholeProfile(String token)
	{
		Object[] profile = sessionData.get(token);
		if (profile == null)
		{
			return(null);
		}
		Object[] newArray = new Object[WHOLE_PROFILE];
		java.lang.System.arraycopy(profile, 0, newArray, 0, WHOLE_PROFILE);
		return(newArray);
	}
	
	public int getLanguage(String token)
	{
		if (sessionData.get(token) == null)
			return 1;
		return(((Integer) sessionData.get(token)[LANGUAGE]).intValue());
	}
	
	public String getClientIp(String token)
	{
		if (sessionData.get(token) == null)
			return null;
		return(((String) sessionData.get(token)[CLIENT_IP]));
	}
	
	public Object[] getSessionData(String token)
	{
		return sessionData.get(token);
	}

	public Object[] getSessionData(int userId)
	{
		Iterator<Map.Entry<String, Object[]>> iter = sessionData.entrySet().iterator();
		while (iter.hasNext()) 
		{
		    Map.Entry<String, Object[]> entry = iter.next();
		    Object[] profile = entry.getValue();
			if (profile[BASIC_PROFILE] == null)
			{
		        iter.remove();
			}
			else if (((User)profile[BASIC_PROFILE]).getIdUser() == userId)
			{
				return(profile);
			}
		}
		return null;
	}

	public int getLanguage(int userId)
	{
		for(String token: sessionData.keySet())
		{
			if (((User)sessionData.get(token)[BASIC_PROFILE]).getIdUser() == userId)
			{
				return(((Integer) sessionData.get(token)[LANGUAGE]).intValue());
			}
		}
		return(Constants.LNG_EN);
	}
	
	public void setLanguage(String token, int languageId)
	{
		Object[] profile = sessionData.get(token);
		if (profile == null)
		{
			return;
		}
		profile[LANGUAGE] = new Integer(languageId);
	}
	
	public void setClientIP(String token, String clientIP)
	{
		Object[] profile = sessionData.get(token);
		if (profile == null)
		{
			return;
		}
		profile[CLIENT_IP] = clientIP;
	}
	
	public void addUser(String token, int languageId) throws Exception
	{
		if (sessionData.get(token) != null)
			return;
		Object[] userData = new Object[SESSION_ELEMENTS];
		DBConnection conn = null;

		try
		{
			conn = DBInterface.connect();
			UserAuth ua = UserAuth.findToken(conn, token);
			if (ua == null)
			{
				throw new Exception("Token '" + token + "' not found");
			}
			userData[BASIC_PROFILE] = new User(conn, ua.getIdUser());
			userData[LANGUAGE] = new Integer(languageId);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		sessionData.put(token, userData);
		return;
	}

	public void removeUser(String token)
	{
		sessionData.remove(token);
	}

	public User getBasicProfile(int userId)
	{
		for(String token: sessionData.keySet())
		{
			if (((User)sessionData.get(token)[BASIC_PROFILE]).getIdUser() == userId)
			{
				return((User)sessionData.get(token)[BASIC_PROFILE]);
			}
		}
		return(null);
	}

	public Object[] getWholeProfile(int userId)
	{
		for(String token: sessionData.keySet())
		{
			if (((User)sessionData.get(token)[BASIC_PROFILE]).getIdUser() == userId)
			{
				return(sessionData.get(token));
			}
		}
		return(null);
	}

	public void addUser(DBConnection conn, int userId, int languageId, HttpServletRequest servletRequest) throws Exception
	{
		UserAuth ua = UserAuth.findUserId(conn, userId);
		if (ua == null)
		{
			throw new Exception("User " + userId + " not found");
		}
		addUser(ua, languageId, servletRequest);
		return;
	}

	public void addUser(UserAuth ua, String language, HttpServletRequest servletRequest) throws Exception
	{
		int languageId = Constants.getLanguageCode(language);
		addUser(ua, languageId, servletRequest);
	}
	
	public void addUser(UserAuth ua, int languageId, HttpServletRequest servletRequest) throws Exception
	{
		log.trace("ua " + ua);
		if (ua == null)
		{
			throw new Exception("UsersAuth is null...");
		}
		
		log.trace("Adding user id " + ua.getIdUser() + " token '" + ua.getLastActiveToken() + 
				  "' using language " + languageId);
		if (sessionData.get(ua.getLastActiveToken()) != null)
		{
			removeUser(ua.getIdUser());
		}

		Object[] userData = new Object[SESSION_ELEMENTS];
		DBConnection conn = null;
		try
		{
			conn = DBInterface.connect();
			userData[BASIC_PROFILE] = new User(conn, ua.getIdUser());
			userData[WHOLE_PROFILE] = userData[BASIC_PROFILE];
			userData[LANGUAGE] = new Integer(languageId);
			userData[CLIENT_IP] = servletRequest.getRemoteAddr();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		sessionData.put(ua.getLastActiveToken(), userData);
	}


	public void removeUser(int userId)
	{
		Iterator<Map.Entry<String, Object[]>> iter = sessionData.entrySet().iterator();
		while (iter.hasNext()) 
		{
		    Map.Entry<String, Object[]> entry = iter.next();
		    if(((User) entry.getValue()[BASIC_PROFILE]).getIdUser() == userId)
			{
		        iter.remove();
		        break;
		    }
		}
		return;
	}

	public void updateSession(int userId, Object[] data, String newToken)
	{
		Iterator<Map.Entry<String, Object[]>> iter = sessionData.entrySet().iterator();
		Object[] sessionItem = null;
		while (iter.hasNext()) 
		{
		    Map.Entry<String, Object[]> entry = iter.next();
		    if(((User) entry.getValue()[BASIC_PROFILE]).getIdUser() == userId)
			{
		    	sessionItem = entry.getValue();
		        iter.remove();
		        break;
		    }
		}
		if ((sessionItem == null) || (sessionItem.length != SESSION_ELEMENTS))
		{
			data[LANGUAGE] = Utils.setLanguageId("EN");
		}
		else
		{
			data[LANGUAGE] = sessionItem[LANGUAGE];
		}
		sessionData.put(newToken, data);
	}

	public void updateSession(String token, Object[] data)
	{
		sessionData.put(token, data);
	}

	public Map<String, Object[]> getAllItems()
	{
		return sessionData;
	}
}
