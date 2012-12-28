package epfl.sweng.services.impl;

import java.io.IOException;

import org.apache.http.HttpMessage;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.services.AuthenticationException;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;
import epfl.sweng.services.SwengResponseHandler;

/**
 * Abstract class to impement a service
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
abstract public class AbstractImplService {

	public static final String SEPARATOR = "/";
	
	private String mBaseUrl;
	private SessionManager mSessionManager;

	public AbstractImplService(String baseUrl, SessionManager sessionManager) {
		this.mBaseUrl = baseUrl;
		this.mSessionManager = sessionManager;
	}

	public String getBaseUrl() {
		return this.mBaseUrl;
	}

	private void addAuthentication(HttpMessage httpMessage) throws ServiceException {
		if (this.mSessionManager != null && this.mSessionManager.isAuthenticated()) {
			try {
				this.mSessionManager.addAuthentication(httpMessage);
			} catch (AuthenticationException e) {
				throw new ServiceException(e);
			}
		}
	}
	
	public SessionManager getSessionManager() {
		return this.mSessionManager;
	}

	protected String execute(HttpUriRequest http, ResponseHandler<String> handler) throws ServiceException {

		Log.d(getClass().getSimpleName(), http.getMethod() + " on " + http.getURI().toString());
		try {
			addAuthentication(http);
			String response = SwengHttpClientFactory.getInstance().execute(http, handler);
			Log.d(getClass().getSimpleName(), http.getMethod() + " response: " + response);
			return response;
	
		} catch (HttpResponseException e) {
			if (e.getStatusCode() != SwengResponseHandler.HTTP_NOT_FOUND) {
				this.mSessionManager.setOffline(true);				
			}
			throw new ServiceException(e);
			
		} catch (IOException e) {
			this.mSessionManager.setOffline(true);
			throw new ServiceException(e);
		}
	}

}