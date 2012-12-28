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
 * @version 4.0.0 - 2012.11.15 - Extract AsyncTask
 */
public class PosterQuestionTask extends AsyncTask<Void, Void, QuizQuestion> {

	private Dialog mProgress;

	private QuizQuestion mQuestion;
	
	private Activity mActivity;
	private QuestionTaskDelegate mDelegate;

	public PosterQuestionTask(Activity activity, QuestionTaskDelegate delegate, QuizQuestion question) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
		
		this.mQuestion = question;
	}

	@Override
	protected void onPreExecute() {
		this.mProgress = ProgressDialog.show(this.mActivity, "", "Submitting...");
		super.onPreExecute();
	}

	@Override
	protected QuizQuestion doInBackground(Void... params) {
		QuestionsService qs = ((QuizApplication) this.mActivity.getApplication()).getQuestionsService();
		try {
			return qs.post(this.mQuestion);

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