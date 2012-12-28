package epfl.sweng.test.utils.requests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Mock for the authentication tests.
 */
public class MockAuthenticationExecutor extends AbstractMockExecutor {

	/**
	 * 
	 */
	public static enum AuthenticationState {
		TokenRequest, AuthenticationRequest, SessionRequest, Unknown
	}

	private AuthenticationState mAuthenticationState = AuthenticationState.TokenRequest;

	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		switch (this.mAuthenticationState) {
			case TokenRequest:
				return tokenRequest();

			case AuthenticationRequest:
				return authenticationRequest(request);

			case SessionRequest:
				return sessionRequest();

			default:
				return generateResponse(HTTP_NOT_FOUND);
		}
	}

	private HttpResponse tokenRequest() throws UnsupportedEncodingException {
		JSONObject jsonToken = new JSONObject();
		try {
			jsonToken.put("token", "4321");
			jsonToken.put("message", "Hi there");

			HttpResponse response = generateResponse(HTTP_OK);
			response.setEntity(new StringEntity(jsonToken.toString()));
			this.mAuthenticationState = AuthenticationState.AuthenticationRequest;

			return response;
		} catch (JSONException e) {
			// Should not happen :-\
			e.printStackTrace();
			return generateResponse(HTTP_NOT_FOUND);
		}
	}

	private HttpResponse authenticationRequest(final HttpRequest request) throws IOException {

		String entity = EntityUtils.toString(((HttpEntityEnclosingRequest) request).getEntity());

		boolean requestkey = entity.contains("requestkey=4321");
		boolean username = entity.contains("username=user");
		boolean password = entity.contains("password=pass");
		
		HttpResponse response = null;

		if (requestkey && username && password) {
			this.mAuthenticationState = AuthenticationState.SessionRequest;
			response = generateResponse(HTTP_FOUND);
			Log.d(getClass().getSimpleName(), "<<<<<<<<<<");
			response.setHeader("Location", "localhost");
			Log.d(getClass().getSimpleName(), ">>>>>>>>>>");
		} else {
			this.mAuthenticationState = AuthenticationState.TokenRequest;
			response = generateResponse(HTTP_NOT_FOUND);
		}
		
		return response;
	}

	private HttpResponse sessionRequest() throws UnsupportedEncodingException {
		JSONObject jsonSession = new JSONObject();
		try {
			jsonSession.put("session", "1234");
			jsonSession.put("message", "Finally :o)");

			HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_OK, "OK");
			response.setEntity(new StringEntity(jsonSession.toString()));
			this.mAuthenticationState = AuthenticationState.Unknown;

			return response;
		} catch (JSONException e) {
			// Should not happen :-\
			e.printStackTrace();
			return generateResponse(HTTP_NOT_FOUND);
		}
	}
}