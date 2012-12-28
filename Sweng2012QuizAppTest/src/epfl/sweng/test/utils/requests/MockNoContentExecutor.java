package epfl.sweng.test.utils.requests;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

/**
 * Mock for the Rating tests.
 */
public class MockNoContentExecutor extends AbstractMockExecutor {
	
	public MockNoContentExecutor() {
		this(false);
	}

	public MockNoContentExecutor(boolean authenticated) {
		super(authenticated);
	}

	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context) {

		return generateResponse(HTTP_NO_CONTENT);
	}
}