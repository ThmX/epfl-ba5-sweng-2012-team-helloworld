package epfl.sweng.test.utils.requests;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

/**
 * Abstract mock for the tests.
 */
abstract public class AbstractMockExecutor extends HttpRequestExecutor {

	public static final String MOCK_HTTP_REQUEST_EXECUTOR = "AbstractMockHttpRequestExecutor";

	public static final int HTTP_OK = 200;
	public static final int HTTP_CREATED = 201;
	public static final int HTTP_NO_CONTENT = 204;
	public static final int HTTP_FOUND = 302;
	public static final int HTTP_NOT_FOUND = 404;

	private boolean mAuthenticated;
	
	public AbstractMockExecutor() {
		this(false);
	}
	
	public AbstractMockExecutor(boolean authenticated) {
		this.mAuthenticated = authenticated;
	}

	@Override
	@SuppressWarnings("unused")
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		// TODO Test if authenticated
		if (this.mAuthenticated) {
			boolean isAuth = false;
			for (Header h : request.getHeaders("Authorization")) {
				if (h.getValue().equals("Tequila 1234")) {
					isAuth = true;
				}
			}
			
			if (!isAuth) {
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_NOT_FOUND, "Not Found");				
			}
		}

		return generateResponse(request);
	}

	public HttpResponse generateResponse(int status) {
		switch (status) {
			case HTTP_OK:
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_OK, "OK");

			case HTTP_CREATED:
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_CREATED, "Created");

			case HTTP_NO_CONTENT:
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_NO_CONTENT, "No Content");

			case HTTP_FOUND:
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_FOUND, "Found");

			default:
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, HTTP_NOT_FOUND, "Not Found");
		}
	}

	public HttpResponse generateResponse(HttpRequest request) {
		String method = request.getRequestLine().getMethod();
		if (method.equals("POST")) {
			return generateResponse(HTTP_CREATED);

		} else if (method.equals("DELETE")) {
			return generateResponse(HTTP_NO_CONTENT);

		} else { // GET
			return generateResponse(HTTP_OK);
		}
	}

	public void setAuthenticated(boolean authenticated) {
		this.mAuthenticated = authenticated;
	}
}