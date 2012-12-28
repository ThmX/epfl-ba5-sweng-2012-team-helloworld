package epfl.sweng.quizzes;

import java.util.List;

import epfl.sweng.FetchActivity;
import epfl.sweng.R;
import epfl.sweng.tasks.FetcherQuizzesTask;
import epfl.sweng.tasks.QuizzesTaskDelegate;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Activity that shows available quizzes.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class ShowAvailableQuizzesActivity extends FetchActivity implements AdapterView.OnItemClickListener,
		QuizzesTaskDelegate {

	private ShowAvailableQuizzesAdapter mShowAvailableQuizzesAdapter;

	private FetcherQuizzesTask mFetcherQuizzesTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_available_quizzes);

		this.mShowAvailableQuizzesAdapter = new ShowAvailableQuizzesAdapter(this);

		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(this.mShowAvailableQuizzesAdapter);
		list.setOnItemClickListener(this);

		fetchQuestion();
	}

	/**
	 * Start fetch process.
	 */
	private void fetchQuestion() {
		fetch();
		this.mFetcherQuizzesTask = new FetcherQuizzesTask(this, this);
		this.mFetcherQuizzesTask.execute();
	}

	@Override
	public void setQuizzes(List<Quiz> quizzes) {
		this.mShowAvailableQuizzesAdapter.setQuizzes(quizzes);
		fetched();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Quiz quiz = (Quiz) view.getTag();
		
		if (quiz == null) {
			return;
		}
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("quiz", quiz);

		Intent intent = new Intent(this, ShowQuizActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
