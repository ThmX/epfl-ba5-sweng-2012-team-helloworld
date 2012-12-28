package epfl.sweng.test.utils;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.QuizApplication;
import epfl.sweng.servercomm.SwengHttpClientFactory;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @see android.test.ActivityInstrumentationTestCase2
 * @param <T> Activity
 */
public class SwengActivityInstrumentationTestCase2<T extends Activity> extends ActivityInstrumentationTestCase2<T> {
	
	private MockHttpClient mHttpClient = new MockHttpClient();
	
	private Solo mSolo;

	public SwengActivityInstrumentationTestCase2(Class<T> activityClass) {
		super(activityClass);
	}
	
	@Override
	public void tearDown() throws Exception {
		SwengHttpClientFactory.setInstance(null);
		this.mSolo.finishOpenedActivities();
	}
	
	@Override
	protected void setUp() throws Exception {
		setUpIntent(null);
	}
	
	protected void setUpIntent(Intent intent) throws Exception {
		super.setUp();
		
		setActivityIntent(intent);
		
		QuizApplication app = (QuizApplication) getActivity().getApplication();
		app.clearSession();

		this.setSolo(new Solo(getInstrumentation(), getActivity()));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void scrubClass(Class testCaseClass) {
	    // ignore
	}

	/**
	 * @return the solo
	 */
	public Solo getSolo() {
		return this.mSolo;
	}

	/**
	 * @param solo the solo to set
	 */
	public void setSolo(Solo solo) {
		this.mSolo = solo;
	}
	
	public MockHttpClient getHttpClient() {
		return this.mHttpClient;
	}

}
