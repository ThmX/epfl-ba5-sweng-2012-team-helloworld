package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.quizratings.QuizRatingStats;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Class Allowing to launch an asynchronous task. Fetch a question from the
 * QuestionFetcher and display it.
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class FetcherRatingsTask extends AsyncTask<Void, Void, QuizRatingStats> {

	private Activity mActivity;
	private RatingTaskDelegate mDelegate;
	private int mID;

	public FetcherRatingsTask(Activity activity, RatingTaskDelegate delegate, int id) {
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
	protected QuizRatingStats doInBackground(Void... params) {
		try {
			RatingsService rs = ((QuizApplication) this.mActivity.getApplication()).getRatingsService();
			return rs.ratings(this.mID);

		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(QuizRatingStats result) {
		this.mDelegate.setRatings(result);
		synchronized (this) {
			notifyAll();
		}
	}
}