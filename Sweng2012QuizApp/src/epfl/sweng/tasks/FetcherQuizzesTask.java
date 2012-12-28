package epfl.sweng.tasks;

import java.util.List;

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
 * Fetch asynchronously quizzes from server.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class FetcherQuizzesTask extends AsyncTask<Void, Void, List<Quiz>> {

	private Dialog mProgress;

	private Activity mActivity;
	private QuizzesTaskDelegate mDelegate;

	public FetcherQuizzesTask(Activity activity, QuizzesTaskDelegate delegate) {
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
	protected List<Quiz> doInBackground(Void... params) {
		try {
			QuizService qs = ((QuizApplication) this.mActivity.getApplication()).getQuizService();
			return qs.get();
		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<Quiz> result) {
		this.mDelegate.setQuizzes(result);
		this.mProgress.dismiss();
		synchronized (this) {
			notifyAll();
		}
	}
}
