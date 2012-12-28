package epfl.sweng.test.utils;

import org.apache.http.HttpMessage;

import epfl.sweng.services.AuthenticationException;
import epfl.sweng.services.SessionManager;

/**
 * Mock of the SessionManager
 */
public class MockSessionManager implements SessionManager {
	
	private static final String SESSION_ID = "1234";
	
	private boolean mAuthenticated;
	
	private boolean mOffline;
	
	public void setAuthenticated(boolean authenticated) {
		this.mAuthenticated = authenticated;
	}

	@Override
	public boolean isAuthenticated() {
		return this.mAuthenticated;
	}

	@Override
	public void clearSession() {
		setAuthenticated(false);
	}

	@Override
	public String getSession() throws AuthenticationException {
		if (!isAuthenticated()) {
			throw new AuthenticationException("");
		}
		
		return SESSION_ID;
	}

	@Override
	public void addAuthentication(HttpMessage httpMessage) throws AuthenticationException {
		httpMessage.setHeader("Authorization", "Tequila " + getSession());
	}
	
	public void setOffline(boolean offline) {
		this.mOffline = offline;
	}

	@Override
	public boolean isOffline() {
		return this.mOffline;
	}

}
