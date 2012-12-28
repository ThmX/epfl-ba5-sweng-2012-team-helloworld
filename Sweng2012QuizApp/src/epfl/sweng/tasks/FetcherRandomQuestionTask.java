package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.services.QuestionsService;
import epfl.sweng.services.ServiceException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Class Allowing to launch an asynchronous task. Fetch a question from the
 * QuestionFetcher and display it.
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class FetcherRandomQuestionTask extends AsyncTask<Void, Void, QuizQuestion> {

	private Dialog mProgress;

	private Activity mActivity;
	private QuestionTaskDelegate mDelegate;

	public FetcherRandomQuestionTask(Activity activity, QuestionTaskDelegate delegate) {
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
	protected QuizQuestion doInBackground(Void... params) {
		try {
			QuestionsService qs = ((QuizApplication) this.mActivity.getApplication()).getQuestionsService();
			return qs.random();

		} catch (ServiceException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(QuizQuestion result) {
		this.mDelegate.setQuestion(result);
		this.mProgress.dismiss();
		synchronized (this) {
			notifyAll();
		}
	}
}