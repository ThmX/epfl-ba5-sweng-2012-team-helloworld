package epfl.sweng.tasks;

import epfl.sweng.QuizApplication;
import epfl.sweng.services.ServiceException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * 
 * @author Gianni
 *
 */
public class OfflineTask extends AsyncTask<Void, Void, Boolean> {
	
	private Dialog mProgress;

	private Activity mActivity;
	private OfflineTaskDelegate mDelegate;
	
	private boolean mOffline;
	
	public OfflineTask(Activity activity, OfflineTaskDelegate delegate, boolean offline) {
		super();
		this.mActivity = activity;
		this.mDelegate = delegate;
		
		this.mOffline = offline;
	}
	
	@Override
	protected void onPreExecute() {
		this.mProgress = ProgressDialog.show(this.mActivity, "", "Syncing...");
		super.onPreExecute();
	}
	
	/**
	 * execute setOffline(..)
	 * @return true in case of error, false otherwise.
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		QuizApplication app = (QuizApplication) this.mActivity.getApplication();
		try {
			app.setOffline(this.mOffline);
		} catch (ServiceException e) {
			return true;
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean error) {
		this.mDelegate.checkError(error);
		this.mProgress.dismiss();
	}
}