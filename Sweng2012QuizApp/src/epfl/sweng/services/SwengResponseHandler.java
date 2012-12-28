package epfl.sweng.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

import android.util.Log;

/**
 * @see org.apache.http.impl.client.BasicResponseHandler
 * @author Gianni
 * 
 */
public class SwengResponseHandler extends BasicResponseHandler {
	public static final int HTTP_OK = 200;
	public static final int HTTP_CREATED = 201;
	public static final int HTTP_NO_CONTENT = 204;

	public static final int HTTP_FOUND = 302;
	
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_UNAUTHORIZED = 401;
	public static final int HTTP_NOT_FOUND = 404;

	private Set<Integer> mStatusCodes;

	public SwengResponseHandler(int... statusCodes) {
		this.mStatusCodes = new HashSet<Integer>();
		for (int sc : statusCodes) {
			this.mStatusCodes.add(sc);
		}
	}

	@Override
	public String handleResponse(HttpResponse response) throws IOException {
		StatusLine sline = response.getStatusLine();
		Log.i("SERVER", "Replied with status code " + sline.getStatusCode());
		if (!this.mStatusCodes.contains(sline.getStatusCode())) {
			throw new HttpResponseException(sline.getStatusCode(), sline.getReasonPhrase() + "("
					+ sline.getStatusCode() + ") instead of " + this.mStatusCodes.toString());
		}

		// XXX Only way to allow the 302 Status Code...
		if ((sline.getStatusCode() == HTTP_FOUND) && (this.mStatusCodes.contains(HTTP_FOUND))) {
			return null;
		}

		return super.handleResponse(response);
	}

}
