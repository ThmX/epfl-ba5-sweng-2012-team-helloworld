package epfl.sweng.showquestions;

import java.util.List;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizAnswer;
import epfl.sweng.quizquestions.QuizQuestion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter that manage views for each answer of a question
 * 
 * @version 1.0.0 - 2012.09.29 - Initial version
 */
public class ShowQuestionsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<QuizAnswer> mAnswers;

	public ShowQuestionsAdapter(Context context) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.mAnswers = null;
	}

	public void setQuestion(QuizQuestion question) {
		if (question != null) {
			this.mAnswers = question.getAnswers();			
		} else {
			this.mAnswers = null;
		}
		notifyDataSetChanged();
	}

	/*
	 * BaseAdapter override methods
	 */

	@Override
	public int getCount() {
		return this.mAnswers != null ? this.mAnswers.size() : 0;
	}

	@Override
	public QuizAnswer getItem(int position) {
		return this.mAnswers != null ? this.mAnswers.get(position) : null;
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
	 * @param convertView View that can be converted
	 * @param parent ViewGroup that contains the view.
	 * @return LinearLayout that will be used to display an answer.
	 */
	private LinearLayout convertLayout(View convertView, ViewGroup parent) {

		LinearLayout layout = null;

		try {
			layout = (LinearLayout) convertView;
		} catch (ClassCastException e) {
			Log.e("ShowQuestionsAdapter", "Couldn't cast LinearLayout", e);
		}

		if (layout == null) {
			layout = (LinearLayout) this.mInflater.inflate(R.layout.activity_show_questions_entry, parent, false);
		}

		return layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout layoutItem = convertLayout(convertView, parent);

		QuizAnswer answer = getItem(position);
		layoutItem.setTag(answer);

		TextView tickTextView = (TextView) layoutItem.findViewById(R.id.textViewTick);
		TextView answerTextView = (TextView) layoutItem.findViewById(R.id.textViewAnswer);

		tickTextView.setText("");
		answerTextView.setText(answer.getText());

		return layoutItem;
	}

}
