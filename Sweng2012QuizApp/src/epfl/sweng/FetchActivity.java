package epfl.sweng;

import android.app.Activity;
import android.util.Log;

/**
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
abstract public class FetchActivity extends Activity {

	private int mFetching;

	public FetchActivity() {
		super();
	}

	/**
	 * Wait until all fetching are done.
	 */
	public void waitForFetch() {
		synchronized (this) {
			while (this.mFetching > 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					Log.e(getClass().getName(), "InterruptedException", e);
				}
			}
		}
	}

	/**
	 * Must be called before a fetch process.
	 */
	protected void fetch() {
		this.mFetching++;
	}

	/**
	 * Must be called after a fetch process.
	 */
	protected void fetched() {
		if (this.mFetching > 0) {
			this.mFetching--;
		}
	
		synchronized (this) {
			notifyAll();
		}
	}

}