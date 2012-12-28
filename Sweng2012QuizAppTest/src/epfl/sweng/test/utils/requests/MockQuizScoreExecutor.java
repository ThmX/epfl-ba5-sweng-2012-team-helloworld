package epfl.sweng.test.utils.requests;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

/**
 * Mock for the QuizMessageException tests.
 */
public class MockQuizScoreExecutor extends AbstractMockExecutor {

	@Override
	public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context)
		throws IOException {

		HttpResponse response = generateResponse(HTTP_OK);

		StringEntity stringEntity = new StringEntity("{ \"score\": 1.25 }");
		response.setEntity(stringEntity);

		return response;
	}
}