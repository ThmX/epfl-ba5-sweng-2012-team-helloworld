package epfl.sweng.showquestions;

import epfl.sweng.FetchActivity;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizAnswer;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.quizratings.QuizRatingStats;
import epfl.sweng.tasks.FetcherRandomQuestionTask;
import epfl.sweng.tasks.FetcherRatingTask;
import epfl.sweng.tasks.FetcherRatingsTask;
import epfl.sweng.tasks.PosterRatingTask;
import epfl.sweng.tasks.QuestionTaskDelegate;
import epfl.sweng.tasks.RatingTaskDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that shows questions and allow the user to answer them.
 * 
 * @version 1.0.0 - 2012.09.29 - Initial version
 * @version 1.0.1 - 2012.10.24 - Fix behaviour
 * @version 4.0.0 - 2012.11.15 - Ratings
 * @version 4.0.1 - 2012.11.15 - Extract AsyncTask
 * @version 5.0.0 - 2012.11.29 - Refactor with FetchActivity
 */
public class ShowQuestionsActivity extends FetchActivity implements AdapterView.OnItemClickListener,
		QuestionTaskDelegate, RatingTaskDelegate {

	private FetcherRandomQuestionTask mFetcherQuestionTask;
	private FetcherRatingTask mFetcherRatingTask;
	private FetcherRatingsTask mFetcherRatingsTask;
	private PosterRatingTask mPosterRatingTask;

	private boolean mAnswered;

	private TextView mQuestionTextView;
	private TextView mRatingStatusTextView;
	private ShowQuestionsAdapter mShowQuestionsAdapter;

	private Button mButtonNextQuestion;
	private Button mButtonLike;
	private Button mButtonDislike;
	private Button mButtonIncorrect;

	private QuizQuestion mQuestion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		this.mQuestionTextView = (TextView) findViewById(R.id.textViewQuestion);
		this.mQuestionTextView.setText("");

		this.mRatingStatusTextView = (TextView) findViewById(R.id.textViewRatingStatus);
		this.mRatingStatusTextView.setText(R.string.not_rated_status);

		this.mShowQuestionsAdapter = new ShowQuestionsAdapter(this);

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(this.mShowQuestionsAdapter);
		list.setOnItemClickListener(this);

		this.mButtonNextQuestion = (Button) findViewById(R.id.buttonNextQuestion);
		this.mButtonNextQuestion.setEnabled(false);

		this.mButtonLike = (Button) findViewById(R.id.buttonLike);
		this.mButtonLike.setEnabled(true);

		this.mButtonDislike = (Button) findViewById(R.id.buttonDislike);
		this.mButtonDislike.setEnabled(true);

		this.mButtonIncorrect = (Button) findViewById(R.id.buttonIncorrect);
		this.mButtonIncorrect.setEnabled(true);

		setRating(QuizRating.none);
		setRatings(new QuizRatingStats(0, 0, 0));

		fetchQuestion();
	}

	/**
	 * 
	 * @param v
	 */
	public void onClickNext(View v) {
		fetchQuestion();
	}

	/**
	 * Updating the likes
	 */
	public void onClickLike(View v) {
		postRating(QuizRating.like);
	}

	/**
	 * Updating the dislikes
	 */
	public void onClickDislike(View v) {
		postRating(QuizRating.dislike);
	}

	/**
	 * Updating the incorrect
	 */
	public void onClickIncorrect(View v) {
		postRating(QuizRating.incorrect);
	}

	/**
	 * Launch the fetching question task
	 */
	private void fetchQuestion() {
		fetch();
		this.mFetcherQuestionTask = new FetcherRandomQuestionTask(this, this);
		this.mFetcherQuestionTask.execute();
	}

	/**
	 * Launch the fetching rating task
	 */
	private void fetchRating() {
		fetch();
		this.mFetcherRatingTask = new FetcherRatingTask(this, this, this.mQuestion.getID());
		this.mFetcherRatingTask.execute();
	}

	/**
	 * Launch the fetching ratings task
	 */
	private void fetchRatings() {
		fetch();
		this.mFetcherRatingsTask = new FetcherRatingsTask(this, this, this.mQuestion.getID());
		this.mFetcherRatingsTask.execute();
	}

	/**
	 * Launch the fetching ratings task
	 */
	private void postRating(QuizRating rating) {
		fetch();

		fetch(); // XXX That's ugly but I couldn't care less right now...
		setRating(rating);

		this.mPosterRatingTask = new PosterRatingTask(this, this, this.mQuestion.getID(), rating);
		this.mPosterRatingTask.execute();
	}

	/**
	 * @return the question
	 */
	public QuizQuestion getQuestion() {
		return this.mQuestion;
	}

	/*
	 * android.widget.AdapterView.OnItemClickListener
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if (this.mAnswered) {
			return;
		}

		QuizAnswer answer = (QuizAnswer) view.getTag();

		TextView tickTextView = (TextView) view.findViewById(R.id.textViewTick);

		if (answer.isSolution()) {
			this.mAnswered = true;
			this.mButtonNextQuestion.setEnabled(true);
			tickTextView.setText(getString(R.string.correct_answer));
		} else {
			tickTextView.setText(getString(R.string.wrong_answer));
		}
	}

	/*
	 * epfl.sweng.tasks.FetcherQuestionTaskDelegate
	 */

	/**
	 * @param question
	 *            the question to set
	 */
	@Override
	public void setQuestion(QuizQuestion question) {
		this.mQuestion = question;

		this.mAnswered = false;

		if (this.mQuestion == null) {
			this.mButtonNextQuestion.setEnabled(true);
			this.mQuestionTextView.setText(getString(R.string.no_question));
		} else {
			this.mButtonNextQuestion.setEnabled(false);
			this.mQuestionTextView.setText(this.mQuestion.getText());
			fetchRatings();
			fetchRating();
		}
		this.mShowQuestionsAdapter.setQuestion(this.mQuestion);

		fetched();
	}

	/*
	 * epfl.sweng.tasks.FetcherRatingTaskDelegate
	 */

	@Override
	public void setRating(QuizRating rating) {
		this.mButtonLike.setEnabled(true);
		this.mButtonDislike.setEnabled(true);
		this.mButtonIncorrect.setEnabled(true);

		if (rating == null) {
			Toast.makeText(this, R.string.error_ratings, Toast.LENGTH_LONG).show();
		} else {
			switch (rating) {
				case like:
					this.mRatingStatusTextView.setText(getString(R.string.like_status));
					this.mButtonLike.setEnabled(false);
					break;

				case dislike:
					this.mRatingStatusTextView.setText(getString(R.string.dislike_status));
					this.mButtonDislike.setEnabled(false);
					break;

				case incorrect:
					this.mRatingStatusTextView.setText(getString(R.string.incorrect_status));
					this.mButtonIncorrect.setEnabled(false);
					break;

				default: // none
					this.mRatingStatusTextView.setText(getString(R.string.not_rated_status));
					break;
			}
		}

		fetched();
	}

	@Override
	public void setRatings(QuizRatingStats stats) {
		if (stats == null) {
			stats = new QuizRatingStats(0, 0, 0);
			Toast.makeText(this, R.string.error_ratings, Toast.LENGTH_LONG).show();
		}

		this.mButtonLike.setText(getString(R.string.like) + " (" + stats.getLikes() + ")");
		this.mButtonDislike.setText(getString(R.string.dislike) + " (" + stats.getDislikes() + ")");
		this.mButtonIncorrect.setText(getString(R.string.incorrect) + " (" + stats.getIncorrects() + ")");

		fetched();
	}

	@Override
	public void rate(boolean error) {
		if (error) {
			Toast.makeText(this, R.string.error_setting_rating, Toast.LENGTH_LONG).show();
		}
		fetchRatings();

		fetched();
	}
}
