package epfl.sweng.test.utils.requests;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

/**
 * Mock for the IOException tests.
 */
public class MockIOExceptionExecutor extends HttpRequestExecutor {
	

	public static final String MOCK_IO_EXCEPTION_HTTP_REQUEST_EXECUTOR = "MockIOExceptionHttpRequestExecutor";

	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		throw new IOException(MOCK_IO_EXCEPTION_HTTP_REQUEST_EXECUTOR);
	}
	
}