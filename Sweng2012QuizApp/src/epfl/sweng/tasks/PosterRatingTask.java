package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Class Allowing to launch an asynchronous task. Post a rating.
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class PosterRatingTask extends AsyncTask<Void, Void, Boolean> {

	private Activity mActivity;
	private RatingTaskDelegate mDelegate;
	
	private QuizRating mRating;
	private int mID;

	public PosterRatingTask(Activity activity, RatingTaskDelegate delegate, int id, QuizRating rating) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
		
		this.mID = id;
		this.mRating = rating;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			RatingsService rs = ((QuizApplication) this.mActivity.getApplication()).getRatingsService();
			rs.rate(this.mID, this.mRating);
			return false;

		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return true;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		this.mDelegate.rate(result);
	}
}