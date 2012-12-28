package epfl.sweng.quizzes;

import java.util.Collections;
import java.util.List;

import epfl.sweng.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter that manage views for each available quiz.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public class ShowAvailableQuizzesAdapter extends BaseAdapter {

	/*
	 * Note: it uses a dummy first element when no quiz is available. I thought
	 * it's better than a TextView in this case (same design FTW).
	 */

	private LayoutInflater mInflater;

	private List<Quiz> mQuizzes;

	public ShowAvailableQuizzesAdapter(Context context) {
		super();
		this.mInflater = LayoutInflater.from(context);

		this.mQuizzes = Collections.emptyList();
	}

	/**
	 * Set quiz collection to show. Use null to mark an error.
	 */
	public void setQuizzes(List<Quiz> quizzes) {
		this.mQuizzes = quizzes;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.mQuizzes == null || this.mQuizzes.isEmpty() ? 1 : this.mQuizzes.size();
	}

	@Override
	public Quiz getItem(int position) {
		return this.mQuizzes == null || this.mQuizzes.isEmpty() ? null : this.mQuizzes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Convert the layout using this steps:
	 * <ul>
	 * <li>Try to convert the convertView if not null</li>
	 * <li>Create a new one if the conversion didn't succeed</li>
	 * </ul>
	 * 
	 * @param convertView
	 *            View that can be converted
	 * @param parent
	 *            ViewGroup that contains the view.
	 * @return LinearLayout that will be used to display an answer.
	 */
	private LinearLayout convertLayout(View convertView, ViewGroup parent) {

		LinearLayout layout = null;

		try {
			layout = (LinearLayout) convertView;
		} catch (ClassCastException e) {
			Log.e("ShowAvailableQuizzesAdapter", "Couldn't cast LinearLayout", e);
		}

		if (layout == null) {
			layout = (LinearLayout) this.mInflater.inflate(R.layout.activity_show_available_quizzes_entry, parent,
				false);
		}

		return layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout layoutItem = convertLayout(convertView, parent);
		TextView title = (TextView) layoutItem.findViewById(android.R.id.text1);

		if (this.mQuizzes == null) {
			title.setText(R.string.fetch_quizzes_failed);
			
		} else if (this.mQuizzes.isEmpty()) {
			title.setText(R.string.no_quizzes);
			
		} else {
			Quiz quiz = getItem(position);
			layoutItem.setTag(quiz);
			title.setText(quiz.getTitle());
		}

		return layoutItem;
	}
}
