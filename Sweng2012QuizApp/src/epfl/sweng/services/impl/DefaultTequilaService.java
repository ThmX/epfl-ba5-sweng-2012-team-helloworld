package epfl.sweng.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.services.SwengResponseHandler;
import epfl.sweng.services.TequilaException;
import epfl.sweng.services.TequilaService;

/**
 * Class to manage the authentication with the Tequila Server
 * 
 * @version 1.0 - 31.10.2012 - Initial version
 */
public class DefaultTequilaService implements TequilaService {
	
	private String mURLLogin;
	private String mURLTequila;
	
	public DefaultTequilaService(String urlLogin, String urlTequila) {
		this.mURLLogin = urlLogin;
		this.mURLTequila = urlTequila;
	}
	
	/* (non-Javadoc)
	 * @see epfl.sweng.services.TequilaService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public String authenticate(String username, String password) throws TequilaException {
		
		// Step 1 & 2
		String token = fetchToken();
		
		// Step 3 & 4
		authenticationRequest(token, username, password);
		
		// Step 5 & 6
		return sessionRequest(token);
	}
	

	private String fetchToken() throws TequilaException {
		HttpGet httpGetToken = new HttpGet(this.mURLLogin);
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			String tokenString = SwengHttpClientFactory.getInstance().execute(httpGetToken, handler);
			JSONObject tokenJSON = new JSONObject(tokenString);
			return tokenJSON.getString("token");
			
		} catch (ClientProtocolException e) {
			throw new TequilaException(e);
		} catch (IOException e) {
			throw new TequilaException(e);
		} catch (JSONException e) {
			throw new TequilaException(e);
		}
	}
	
	private void authenticationRequest(String token, String username, String password) throws TequilaException {
		HttpPost httpPostAuthenticationRequest = new HttpPost(this.mURLTequila);
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_FOUND);
		
		List<NameValuePair> list = new LinkedList<NameValuePair>();
		list.add(new BasicNameValuePair("requestkey", token));
		list.add(new BasicNameValuePair("username", username));
		list.add(new BasicNameValuePair("password", password));
		
		try {
			httpPostAuthenticationRequest.setEntity(new UrlEncodedFormEntity(list));
		} catch (UnsupportedEncodingException e) {
			throw new TequilaException(e);
		}
		
		
		try {
			SwengHttpClientFactory.getInstance().execute(httpPostAuthenticationRequest, handler);
			
		} catch (ClientProtocolException e) {
			throw new TequilaException(e);
		} catch (IOException e) {
			throw new TequilaException(e);
		}
	}
	
	private String sessionRequest(String token) throws TequilaException {
		HttpPost httpPostSessionRequest = new HttpPost(this.mURLLogin);
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			JSONObject tokenJSON = new JSONObject();
			tokenJSON.put("token", token);
			httpPostSessionRequest.setEntity(new StringEntity(tokenJSON.toString()));
		} catch (JSONException e) {
			throw new TequilaException(e);
		} catch (UnsupportedEncodingException e) {
			throw new TequilaException(e);
		}
		
		httpPostSessionRequest.setHeader("Content-type", "application/json");
		try {
			
			String sessionString = SwengHttpClientFactory.getInstance().execute(httpPostSessionRequest, handler);
			JSONObject sessionJSON = new JSONObject(sessionString);
			
			return sessionJSON.getString("session");
			
		} catch (ClientProtocolException e) {
			throw new TequilaException(e);
		} catch (IOException e) {
			throw new TequilaException(e);
		} catch (JSONException e) {
			throw new TequilaException(e);
		}
	}
	
}
