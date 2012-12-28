package epfl.sweng.services;

import org.apache.http.HttpMessage;

/**
 * Allow to manager the session
 */
public interface SessionManager {
	
	boolean isAuthenticated();
	
	void clearSession();

	String getSession() throws AuthenticationException;

	void addAuthentication(HttpMessage httpMessage) throws AuthenticationException;
	
	void setOffline(boolean offline) throws ServiceException;
	
	boolean isOffline();
	
}
