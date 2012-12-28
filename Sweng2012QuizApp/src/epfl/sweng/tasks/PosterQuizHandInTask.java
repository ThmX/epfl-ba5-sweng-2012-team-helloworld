package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.services.QuizService;
import epfl.sweng.services.ServiceException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Post asynchronously a quiz "hand in" to server.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class PosterQuizHandInTask extends AsyncTask<Void, Void, Double> {

	private Dialog mProgress;

	private Activity mActivity;
	private Quiz mQuiz;

	private QuizTaskDelegate mDelegate;

	public PosterQuizHandInTask(Activity activity, QuizTaskDelegate delegate, Quiz quiz) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
		this.mQuiz = quiz; 
	}

	@Override
	protected void onPreExecute() {
		this.mProgress = ProgressDialog.show(this.mActivity, "", "Submitting...");
		super.onPreExecute();
	}

	@Override
	protected Double doInBackground(Void... params) {
		try {
			QuizService qs = ((QuizApplication) this.mActivity.getApplication()).getQuizService();
			return qs.post(this.mQuiz);				

		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Double result) {
		this.mDelegate.setScore(result);
		this.mProgress.dismiss();
		synchronized (this) {
			notifyAll();
		}
	}
}
