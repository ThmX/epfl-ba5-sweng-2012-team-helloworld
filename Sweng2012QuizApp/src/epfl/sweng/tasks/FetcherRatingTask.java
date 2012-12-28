package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Class Allowing to launch an asynchronous task. Fetch a rating.
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class FetcherRatingTask extends AsyncTask<Void, Void, QuizRating> {
	
	private Activity mActivity;
	private RatingTaskDelegate mDelegate;
	
	private int mID;

	public FetcherRatingTask(Activity activity, RatingTaskDelegate delegate, int id) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
		this.mID = id;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected QuizRating doInBackground(Void... params) {
		try {
			RatingsService rs = ((QuizApplication) this.mActivity.getApplication()).getRatingsService();
			return rs.rating(this.mID);

		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(QuizRating result) {
		this.mDelegate.setRating(result);
		synchronized (this) {
			notifyAll();
		}
	}
}