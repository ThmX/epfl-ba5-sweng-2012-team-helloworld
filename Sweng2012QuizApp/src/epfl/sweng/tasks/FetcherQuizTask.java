package epfl.sweng.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import epfl.sweng.QuizApplication;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.services.QuizService;
import epfl.sweng.services.ServiceException;

/**
 * Fetch asynchronously a quiz from server.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class FetcherQuizTask extends AsyncTask<Integer, Void, Quiz> {

	private Dialog mProgress;

	private Activity mActivity;
	private QuizTaskDelegate mDelegate;

	public FetcherQuizTask(Activity activity, QuizTaskDelegate delegate) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
	}

	@Override
	protected void onPreExecute() {
		this.mProgress = ProgressDialog.show(this.mActivity, "", "Fetching...");
		super.onPreExecute();
	}

	@Override
	protected Quiz doInBackground(Integer... params) {
		try {
			QuizService qs = ((QuizApplication) this.mActivity.getApplication()).getQuizService();
			return qs.get(params[0]);
		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(Quiz result) {
		this.mDelegate.setQuiz(result);
		this.mProgress.dismiss();
		synchronized (this) {
			notifyAll();
		}
	}
}
