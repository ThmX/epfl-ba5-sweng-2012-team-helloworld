package epfl.sweng.test.utils;

import epfl.sweng.servercomm.SwengHttpClientFactory;

import android.test.AndroidTestCase;

/**
 * Sweng TestCase
 */
public class SwengTestCase extends AndroidTestCase {
	
	private MockHttpClient mHttpClient = new MockHttpClient();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SwengHttpClientFactory.setInstance(this.mHttpClient);
	}

	@Override
	protected void tearDown() throws Exception {
		SwengHttpClientFactory.setInstance(null);
		super.tearDown();
	}
	
	public MockHttpClient getHttpClient() {
		return this.mHttpClient;
	}
}
