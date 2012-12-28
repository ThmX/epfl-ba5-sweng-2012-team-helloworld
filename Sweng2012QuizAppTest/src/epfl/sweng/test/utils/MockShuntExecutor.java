package epfl.sweng.test.utils;

import java.io.IOException;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import epfl.sweng.test.utils.requests.MockJSONExecutor;

/**
 * Allow to switch the Executor
 */
public class MockShuntExecutor extends HttpRequestExecutor {

	private HttpRequestExecutor[] mExecutors = {new MockJSONExecutor()};
	private int mIndex = 0;

	@Override
	public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context)
		throws IOException, HttpException {

		if (this.mIndex >= this.mExecutors.length) {
			this.mIndex = 0;
		}

		return this.mExecutors[this.mIndex++].execute(request, conn, context);
	}

	public void setExecutors(HttpRequestExecutor... executors) {
		this.mExecutors = executors;
	}
}
