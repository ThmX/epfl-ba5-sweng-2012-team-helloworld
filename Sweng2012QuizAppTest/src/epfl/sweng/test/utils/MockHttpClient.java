package epfl.sweng.test.utils;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpRequestExecutor;

import epfl.sweng.servercomm.SwengHttpClientFactory;

/**
 * To use this, call SwengHttpClientFactory.setInstance(new MockHttpClient()) in
 * your testing code. Remember that the app always has to use
 * SwengHttpClientFactory.getInstance() if it needs an HttpClient.
 **/
public class MockHttpClient extends DefaultHttpClient {

	/**
	 * 
	 */
	public static enum ResponseType {
		Authentication, Question, QuizMessageException, JSONException, IOException
	}

	private MockShuntExecutor mShuntExecutor = new MockShuntExecutor();
	
	public MockHttpClient() {
		setRedirectHandler(SwengHttpClientFactory.REDIRECT_NO_FOLLOW);
	}

	@Override
	protected HttpRequestExecutor createRequestExecutor() {
		return this.mShuntExecutor;
	}

	public void setExecutors(HttpRequestExecutor... executors) {
		this.mShuntExecutor.setExecutors(executors);
	}

}