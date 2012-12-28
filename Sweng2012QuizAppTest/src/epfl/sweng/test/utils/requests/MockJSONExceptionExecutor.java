package epfl.sweng.test.utils.requests;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

/**
 * Mock for the JSONException tests.
 */
public class MockJSONExceptionExecutor extends AbstractMockExecutor {

	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		HttpResponse response = super.execute(request, conn, context);
		
		response.setEntity(new StringEntity("{ name:"));
		
		return response;
	}

}