package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.services.TequilaException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Asynchronous task that execute authentication in background.
 * 
 * @version 4.0.0 - 2012.11.15 - Extract AsyncTask
 */
public class AuthenticationTask extends AsyncTask<Void, Void, String> {

	private String mUsername;
	private String mPassword;

	private Dialog mProgress;

	private Activity mActivity;
	private AuthenticationTaskDelegate mDelegate;

	public AuthenticationTask(Activity activity, AuthenticationTaskDelegate delegate,
			String username, String password) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;

		this.mUsername = username;
		this.mPassword = password;
	}

	@Override
	protected void onPreExecute() {
		this.mProgress = ProgressDialog.show(this.mActivity, "", "Authenticating...");
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		QuizApplication app = (QuizApplication) this.mActivity.getApplication();
		try {
			app.authenticate(this.mUsername, this.mPassword);
		} catch (TequilaException e) {
			return e.getCause().getMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		this.mProgress.dismiss();
		this.mDelegate.authenticationFinished(result);
	}
}