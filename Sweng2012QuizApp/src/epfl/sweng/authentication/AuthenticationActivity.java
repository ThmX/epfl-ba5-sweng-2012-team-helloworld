package epfl.sweng.authentication;

import epfl.sweng.FetchActivity;
import epfl.sweng.R;
import epfl.sweng.tasks.AuthenticationTask;
import epfl.sweng.tasks.AuthenticationTaskDelegate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity used to log in to Tequila.
 * 
 * @version 3.0.0 - 2012.11.01 - Initial version
 * @version 4.0.0 - 2012.11.15 - Extract AsyncTask
 */
public class AuthenticationActivity extends FetchActivity implements AuthenticationTaskDelegate {

	private EditText mAuthUsername;
	private EditText mAuthPassword;
	private Button mButtonLogin;

	private AuthenticationTask mAuthTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		this.mAuthTask = null;

		TextWatcher updateWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// I haz nothing to do. lol.
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Neither do I XO
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateButtonLogin();
			}
		};

		this.mAuthUsername = (EditText) findViewById(R.id.authUsername);
		this.mAuthUsername.addTextChangedListener(updateWatcher);

		this.mAuthPassword = (EditText) findViewById(R.id.authPassword);
		this.mAuthPassword.addTextChangedListener(updateWatcher);

		this.mButtonLogin = (Button) findViewById(R.id.buttonLogin);
		this.mButtonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initiateLogin();
			}
		});
		
		updateButtonLogin();
	}

	/**
	 * Ensure that login button is only enabled if both text fields are
	 * non-empty.
	 */
	void updateButtonLogin() {
		this.mButtonLogin.setEnabled(this.mAuthUsername.getText().length() > 0
				&& this.mAuthPassword.getText().length() > 0);
	}

	/**
	 * Call asynchronous worker to initiate authentication.
	 */
	void initiateLogin() {
		String username = this.mAuthUsername.getText().toString();
		String password = this.mAuthPassword.getText().toString();
		
		fetch();
		this.mAuthTask = new AuthenticationTask(this, this, username, password);
		this.mAuthTask.execute();
	}

	/**
	 * Close activity if authentication succeeded. Otherwise, show error and clear fields.
	 */
	@Override
	public void authenticationFinished(String failureCause) {
		fetched();
		if (failureCause == null) {
			finish();
		} else {
			Toast.makeText(this, failureCause, Toast.LENGTH_SHORT).show();
			this.mAuthUsername.getText().clear();
			this.mAuthPassword.getText().clear();
			// TODO give focus to authUsername?
		}
	}
}
