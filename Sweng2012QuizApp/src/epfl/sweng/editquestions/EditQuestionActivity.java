package epfl.sweng.editquestions;

import java.util.Set;

import epfl.sweng.FetchActivity;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizAnswerBuilder;
import epfl.sweng.quizquestions.QuizBuildingException;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizquestions.QuizQuestionBuilder;
import epfl.sweng.tasks.PosterQuestionTask;
import epfl.sweng.tasks.QuestionTaskDelegate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity that allows to create and edit a question.
 * 
 * @version 2.0.0 - 2012.10.14 - Initial version
 * @version 4.0.0 - 2012.11.15 - Extract AsyncTask
 * @version 4.0.1 - 2012.11.15 - Merge with exam1
 */
public class EditQuestionActivity extends FetchActivity implements QuestionTaskDelegate {

	private PosterQuestionTask mPosterTask;
	private boolean mPosting;

	private QuizQuestionBuilder mBuilder;

	private EditText mEditTextQuestion;
	private EditText mEditTextTags;
	private EditQuestionAdapter mEditQuestionAdapter;
	private Button mButtonSubmit;
	private ListView mList;
	private Button mButtonAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);
		
		TextWatcher textWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Nothing to do :o)
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Nothing to do :o)
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				updateSubmitButtonState();
			}
		};

		this.mEditTextQuestion = (EditText) findViewById(R.id.editTextQuestion);
		this.mEditTextQuestion.addTextChangedListener(textWatcher);
		this.mEditTextTags = (EditText) findViewById(R.id.editTextTags);
		this.mEditTextTags.addTextChangedListener(textWatcher);

		this.mEditQuestionAdapter = new EditQuestionAdapter(this);

		this.mList = (ListView) findViewById(android.R.id.list);
		this.mList.setAdapter(this.mEditQuestionAdapter);

		this.mButtonAdd = (Button) findViewById(R.id.buttonAdd);
		this.mButtonAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					getEditQuestionAdapter().newAnswer();
				} catch (QuizBuildingException e) {
					StringBuilder builder = new StringBuilder();
					for (String s : e.getAudits()) {
						builder.append(s + "\n");
					}
					builder.deleteCharAt(builder.length() - 1);

					Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		this.mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);
		this.mButtonSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitQuestion();
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("question")) {
			this.mBuilder = new QuizQuestionBuilder((QuizQuestion) extras.getSerializable("question"));
			updateUI();

		} else {
			clearBuilder();
		}
		this.mEditQuestionAdapter.setQuestionBuilder(this.mBuilder);

		updateSubmitButtonState();
		this.mEditTextQuestion.requestFocus();
	}
	
	QuizQuestion generateQuestion() throws QuizBuildingException {
		QuizQuestion question = this.mBuilder.generate();
		
		Set<String> audits = question.auditErrorsAsText(0); // TODO depth?
		
		audits.remove("Owner is unspecified!"); // Owner
		audits.remove("ID must be a positive integer!"); // ID
		
		if (audits.size() > 0) {
			throw new QuizBuildingException(audits);
		}
		
		return question;
	}

	/**
	 * Launch the fetching task
	 */
	void submitQuestion() {

		try {
			updateBuilder();
			
			QuizQuestion question = generateQuestion();
			
			Log.i(EditQuestionActivity.class.getName(), question.toString());

			// TODO Submit using the ASyncTask
			this.mPosting = true;
			this.mPosterTask = new PosterQuestionTask(this, this, question);
			this.mPosterTask.execute();

		} catch (QuizBuildingException e) {
			StringBuilder builder = new StringBuilder();
			for (String s : e.getAudits()) {
				builder.append(s + "\n");
			}
			builder.deleteCharAt(builder.length() - 1);

			Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void updateSubmitButtonState() {
		try {
			updateBuilder();
			generateQuestion();
			this.mButtonSubmit.setEnabled(true);
			
		} catch (QuizBuildingException e) {
			this.mButtonSubmit.setEnabled(false);
		}
	}

	/**
	 * Wait while the send is on going
	 */
	public void waitForSend() {
		synchronized (this.mPosterTask) {
			while (this.mPosting) {
				try {
					this.mPosterTask.wait();
				} catch (InterruptedException e) {
					Log.e(getClass().getName(), "InterruptedException", e);
				}
			}
		}
	}

	/**
	 * Create a new Builder and
	 */
	private void clearBuilder() {		
		this.mBuilder = new QuizQuestionBuilder();
		
		this.mEditTextQuestion.setText("");
		this.mEditTextTags.setText("");
		this.mEditQuestionAdapter.setQuestionBuilder(this.mBuilder);
		
		this.mPosting = false;
		updateUI();
	}

	/**
	 * Update the UI with the QuestionBuilder data
	 */
	private void updateUI() {
		this.mEditTextQuestion.setText(this.mBuilder.getText());

		StringBuilder builder = new StringBuilder();
		for (String t : this.mBuilder.getTags()) {
			builder.append(t);
		}
		this.mEditTextTags.setText(builder.toString());

		this.mEditQuestionAdapter.setQuestionBuilder(this.mBuilder);
	}

	/**
	 * Update the QuestionBuilder with data from the UI
	 */
	private void updateBuilder() {
		this.mBuilder.setText(this.mEditTextQuestion.getText().toString());
		this.mBuilder.setTags(this.mEditTextTags.getText().toString());
	}

	EditQuestionAdapter getEditQuestionAdapter() {
		return this.mEditQuestionAdapter;
	}

	@Override
	public void setQuestion(QuizQuestion result) {
		clearBuilder();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Checks the consistency of the graphical user interface.
	 * 
	 * @return the number of inconsistencies found.
	 */
	public int auditErrors() {

		int audit = 0;

		// Exercise 2: Verify Your EditTexts (20 points)
		audit += auditErrorsEditTexts();

		// Exercise 3: Verify Your Buttons (20 points)
		audit += auditErrorsButtons();

		// Exercise 4: Verify the Consistency of Your Answers (20 points)
		audit += auditErrorsAnswers();
		
		// Exercise 5: Verify the State of the Submit Button (30 points)
		audit += auditErrorsSubmit();

		return audit;
	}

	private boolean checkEditText(EditText editText, int hintID) {
		return editText.getHint().toString().equals(getString(hintID)) && editText.getVisibility() == View.VISIBLE;
	}

	/**
	 * Exercise 2: Verify Your EditTexts (20 points)
	 * 
	 * @return the audit
	 */
	private int auditErrorsEditTexts() {
		int audit = 0;

		if (!checkEditText(this.mEditTextQuestion, R.string.type_in_question)) {
			audit++;
		}

		if (!checkEditText(this.mEditTextTags, R.string.type_in_tags)) {
			audit++;
		}

		for (int i = 0; i < this.mList.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) this.mList.getChildAt(i);
			EditText editText = (EditText) layout.findViewById(R.id.editTextAnswer);
			if (!checkEditText(editText, R.string.type_in_answer)) {
				return audit + 1;
			}
		}

		return audit;
	}

	private boolean checkButton(Button button, int textID) {
		return button.getText().toString().equals(getString(textID)) && button.getVisibility() == View.VISIBLE;
	}

	/**
	 * Exercise 3: Verify Your Buttons (20 points)
	 * 
	 * @return the audit
	 */
	private int auditErrorsButtons() {
		int audit = 0;

		if (!checkButton(this.mButtonAdd, R.string.add_answer)) {
			audit++;
		}

		if (!checkButton(this.mButtonSubmit, R.string.submit)) {
			audit++;
		}

		boolean checkRemove = true;
		boolean checkTick = true;
		for (int i = 0; i < this.mList.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) this.mList.getChildAt(i);

			Button btRemove = (Button) layout.findViewById(R.id.buttonRemoveAnswer);
			if (checkRemove && !checkButton(btRemove, R.string.remove_answer)) {
				checkRemove = false;
				audit++;
			}

			Button btTick = (Button) layout.findViewById(R.id.buttonWrongCorrectAnswer);
			if (checkTick) {
				if (!(checkButton(btTick, R.string.correct_answer) || checkButton(btTick, R.string.wrong_answer))) {
					checkTick = false;
					audit++;
				}
			}
		}

		return audit;
	}

	/**
	 * Exercise 4: Verify the Consistency of Your Answers (20 points)
	 * 
	 * @return the audit
	 */
	private int auditErrorsAnswers() {
		int countTick = 0;

		for (int i = 0; i < this.mList.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) this.mList.getChildAt(i);

			Button btTick = (Button) layout.findViewById(R.id.buttonWrongCorrectAnswer);
			if (btTick.getText().toString().equals(getString(R.string.correct_answer))) {
				countTick++;
			}
		}

		return countTick > 1 ? 1 : 0;
	}
	
	/**
	 * Exercise 5: Verify the State of the Submit Button (30 points)
	 * 
	 * @return the audit
	 */
	private int auditErrorsSubmit() {
		boolean isValid = false;
		
		QuizQuestionBuilder builder = new QuizQuestionBuilder();
		builder.setText(this.mEditTextQuestion.getText().toString());
		builder.setTags(this.mEditTextTags.getText().toString());
		
		for (int i = 0; i < this.mList.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) this.mList.getChildAt(i);

			EditText editText = (EditText) layout.findViewById(R.id.editTextAnswer);
			Button btTick = (Button) layout.findViewById(R.id.buttonRemoveAnswer);
			QuizAnswerBuilder answer;
			try {
				answer = builder.newAnswer();
				answer.setText(editText.getText().toString());
				answer.setSolution(btTick.getText().toString().equals(getString(R.string.correct_answer)));
			} catch (QuizBuildingException e) {
				return 1;
			}
		}
		
		try {
			
			QuizQuestion question = builder.generate();
			
			Set<String> audits = question.auditErrorsAsText(0); // TODO depth?

			audits.remove("Owner is unspecified!"); // Owner
			audits.remove("ID must be a positive integer!"); // ID

			if (audits.size() > 0) {
				throw new QuizBuildingException(audits);
			}
			
			isValid = true;
		} catch (QuizBuildingException e) {
			// Nothing to do :o)
		}
		
		return isValid ^ this.mButtonSubmit.isEnabled() ? 1 : 0;
	}
}
