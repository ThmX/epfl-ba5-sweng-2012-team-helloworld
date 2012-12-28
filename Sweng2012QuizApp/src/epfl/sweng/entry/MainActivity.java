package epfl.sweng.entry;

import epfl.sweng.FetchActivity;
import epfl.sweng.QuizApplication;
import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.tasks.OfflineTask;
import epfl.sweng.tasks.OfflineTaskDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Main activity that allows to select what to do with the App 
 * 
 * @version 1.1.0 - 2012.11.01 - Added authentication
 * @version 1.0.0 - 2012.10.14 - Initial version
 */
public class MainActivity extends FetchActivity implements OfflineTaskDelegate {

	private OfflineTask mOfflineTask;
	private CheckBox mOfflineCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OnClickListener gotoListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = (Intent) view.getTag();
				startActivity(intent);
			}
		};
		
		Button buttonShowRandom = (Button) findViewById(R.id.buttonShowRandom);
		buttonShowRandom.setTag(new Intent(this, ShowQuestionsActivity.class));
		buttonShowRandom.setOnClickListener(gotoListener);

		Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonSubmit.setTag(new Intent(this, EditQuestionActivity.class));
		buttonSubmit.setOnClickListener(gotoListener);
		
		Button buttonShowQuizzes = (Button) findViewById(R.id.buttonShowQuizzes);
		buttonShowQuizzes.setTag(new Intent(this, ShowAvailableQuizzesActivity.class));
		buttonShowQuizzes.setOnClickListener(gotoListener);
		
		Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
		buttonLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				QuizApplication app = (QuizApplication) getApplication();
				app.clearSession();
				ensureAuthentication();
			}
		});
		
		this.mOfflineCheckBox = (CheckBox) findViewById(R.id.offlineModeCheckBox);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ensureAuthentication();
		
		QuizApplication app = (QuizApplication) getApplication();
		this.mOfflineCheckBox.setChecked(app.isOffline());
	}
	
	void ensureAuthentication() {
		QuizApplication app = (QuizApplication) getApplication();
		if (!app.isAuthenticated()) {
			Intent intent = new Intent(this, AuthenticationActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * execute the offline task
	 */
	public void onCheckboxClicked(View v) {
		fetch();
		
		this.mOfflineTask = new OfflineTask(this, this, this.mOfflineCheckBox.isChecked());
		this.mOfflineTask.execute();
	}

	/**
	 * if there is an error, invert the checkBox to its precedent state
	 */
	@Override
	public void checkError(boolean error) {
		if (error) {
			QuizApplication app = (QuizApplication) getApplication();
			this.mOfflineCheckBox.setChecked(app.isOffline());
		}
		
		fetched();
	}
}
