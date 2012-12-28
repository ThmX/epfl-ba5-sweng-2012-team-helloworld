package epfl.sweng.quizzes;

import epfl.sweng.FetchActivity;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.FetcherQuizTask;
import epfl.sweng.tasks.PosterQuizHandInTask;
import epfl.sweng.tasks.QuizTaskDelegate;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that shows questions of a quiz. Once the user has finished, he may
 * upload his results to see his score.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class ShowQuizActivity extends FetchActivity implements AdapterView.OnItemClickListener, QuizTaskDelegate {

	private TextView mQuestionTextView;

	private ShowQuizAdapter mShowQuizAdapter;

	private FetcherQuizTask mFetcherQuizTask;
	private PosterQuizHandInTask mPosterQuizHandInTask;

	private Button mButtonPreviousQuestion;
	private Button mButtonNextQuestion;
	private Button mButtonHandInQuestion;

	private Quiz mQuiz;
	private int mQuestionIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_quiz);

		this.mQuestionTextView = (TextView) findViewById(R.id.textViewQuestion);
		this.mQuestionTextView.setText("");

		this.mShowQuizAdapter = new ShowQuizAdapter(this);

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(this.mShowQuizAdapter);
		list.setOnItemClickListener(this);

		this.mButtonPreviousQuestion = (Button) findViewById(R.id.buttonPreviousQuestion);
		this.mButtonPreviousQuestion.setEnabled(false);

		this.mButtonNextQuestion = (Button) findViewById(R.id.buttonNextQuestion);
		this.mButtonNextQuestion.setEnabled(false);

		this.mButtonHandInQuestion = (Button) findViewById(R.id.buttonHandInQuiz);
		this.mButtonHandInQuestion.setEnabled(false);

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("quiz")) {
			this.mQuiz = (Quiz) extras.getSerializable("quiz");
			fetchQuiz();
		} else {
			Log.d(getClass().getSimpleName(), "No extras");
			setQuiz(null);
		}
	}

	/**
	 * Start fetch process.
	 */
	private void fetchQuiz() {
		fetch();
		this.mFetcherQuizTask = new FetcherQuizTask(this, this);
		this.mFetcherQuizTask.execute(this.mQuiz.getID());
	}

	/**
	 * Start posting process.
	 */
	private void postResults() {
		fetch();
		this.mPosterQuizHandInTask = new PosterQuizHandInTask(this, this, this.mQuiz);
		this.mPosterQuizHandInTask.execute(); // TODO call args according to poster
	}

	/**
	 * Refresh GUI according to current question (no question yield a disabled
	 * screen).
	 */
	private void updateDisplayedQuestion() {
		if ((this.mQuiz == null) || (this.mQuiz.getCount() <= 0)) {

			// Nothing to show.
			this.mQuestionTextView.setText("");
			this.mShowQuizAdapter.setQuestion(null, null);
			this.mButtonPreviousQuestion.setEnabled(false);
			this.mButtonNextQuestion.setEnabled(false);
			this.mButtonHandInQuestion.setEnabled(false);

		} else {

			QuizQuestion question = this.mQuiz.getQuestion(this.mQuestionIndex);
			Integer userAnswer = this.mQuiz.getAnswer(this.mQuestionIndex);
			
			Log.i(getClass().getSimpleName(), this.mQuestionIndex + " - " + question + " - " + userAnswer);

			this.mQuestionTextView.setText(question.getText());
			this.mShowQuizAdapter.setQuestion(question, userAnswer);
			this.mButtonPreviousQuestion.setEnabled(true);
			this.mButtonNextQuestion.setEnabled(true);
			this.mButtonHandInQuestion.setEnabled(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		QuizQuestion question = this.mQuiz.getQuestion(this.mQuestionIndex);
		Integer userAnswer = this.mQuiz.getAnswer(this.mQuestionIndex);
		
		// Click on current choice to undo.
		userAnswer = (userAnswer != null) && (userAnswer == position) ? null : position; 
		
		this.mQuiz.setAnswer(this.mQuestionIndex, userAnswer);
		this.mShowQuizAdapter.setQuestion(question, userAnswer);
	}

	/**
	 * Move to the previous question (wrap if beginning reached).
	 */
	public void onClickPrevious(View v) {
		--this.mQuestionIndex;
		if (this.mQuestionIndex < 0) {
			this.mQuestionIndex = this.mQuiz.getCount() - 1;
		}

		updateDisplayedQuestion();
	}

	/**
	 * Move to the next question (wrap if end reached).
	 */
	public void onClickNext(View v) {
		++this.mQuestionIndex;
		if (this.mQuestionIndex >= this.mQuiz.getCount()) {
			this.mQuestionIndex = 0;
		}

		updateDisplayedQuestion();
	}

	/**
	 * End and submit quiz.
	 */
	public void onClickHandInQuiz(View v) {
		postResults();
	}

	@Override
	public void setQuiz(Quiz quiz) {
		this.mQuiz = quiz;
		this.mQuestionIndex = 0;
		updateDisplayedQuestion();
		fetched();
		
		if (quiz == null) {
			String msg = getString(R.string.fetch_quiz_failed);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			this.mQuestionTextView.setText(msg);
		}
	}

	@Override
	public void setScore(Double score) {
		Builder builder = new AlertDialog.Builder(this);
		
		if (score == null) {
			builder.setMessage("An error occurred while handing in your answers");
		} else {
			builder.setMessage("Your score is " + String.format("%.2f", score));
		}
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.show();
		fetched();
	}

}

