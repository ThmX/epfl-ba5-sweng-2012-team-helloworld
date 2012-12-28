package epfl.sweng.editquestions;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizAnswerBuilder;
import epfl.sweng.quizquestions.QuizBuildingException;
import epfl.sweng.quizquestions.QuizQuestionBuilder;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Adapter that manage views for each answer of a question
 * 
 * @version 1.0.0 - 2012.09.29 - Initial version
 */
public class EditQuestionAdapter extends BaseAdapter {
	
	private EditQuestionActivity mEditQuestionActivity;
	private LayoutInflater mInflater;

	private QuizQuestionBuilder mQuestionBuilder;

	public EditQuestionAdapter(EditQuestionActivity editQuestionActivity) {
		super();
		this.mEditQuestionActivity = editQuestionActivity;
		
		this.mInflater = LayoutInflater.from(this.mEditQuestionActivity);
	}
	
	public void setQuestionBuilder(QuizQuestionBuilder questionBuilder) {
		this.mQuestionBuilder = questionBuilder;
		
		notifyDataSetChanged();
	}

	public void newAnswer() throws QuizBuildingException {
		if (this.mQuestionBuilder != null) {
			this.mQuestionBuilder.newAnswer();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
		this.getEditQuestionActivity().updateSubmitButtonState();
		super.notifyDataSetChanged();
	}

	/*
	 * BaseAdapter override methods
	 */

	@Override
	public int getCount() {
		return this.mQuestionBuilder != null ? this.mQuestionBuilder.getAnswerCount() : 0;
	}

	@Override
	public QuizAnswerBuilder getItem(int position) {
		return this.mQuestionBuilder != null ? this.mQuestionBuilder.getAnswer(position) : null;
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
			layout = (LinearLayout) this.mInflater.inflate(R.layout.activity_edit_question_entry, parent, false);
		}

		return layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout layoutItem = convertLayout(convertView, parent);

		final QuizAnswerBuilder answer = getItem(position);
		layoutItem.setTag(answer);

		final EditText editTextAnswer = (EditText) layoutItem.findViewById(R.id.editTextAnswer);
		if (editTextAnswer.getTag() != null) {
			editTextAnswer.removeTextChangedListener((TextWatcher) editTextAnswer.getTag());
		}
		TextWatcher watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Nothing to do
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Nothing to do
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				answer.setText(s.toString());
				getEditQuestionActivity().updateSubmitButtonState();
			}
		};
		editTextAnswer.setTag(watcher);
		editTextAnswer.addTextChangedListener(watcher);
		
		final Button buttonWrongCorrectAnswer = (Button) layoutItem.findViewById(R.id.buttonWrongCorrectAnswer);
		buttonWrongCorrectAnswer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				answer.switchSolution();
				notifyDataSetChanged();
			}
		});
		
		final Button buttonRemoveAnswer = (Button) layoutItem.findViewById(R.id.buttonRemoveAnswer);
		buttonRemoveAnswer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					answer.remove();
				} catch (QuizBuildingException e) {
					StringBuilder builder = new StringBuilder();
					for (String s: e.getAudits()) {
						builder.append(s + "\n");
					}
					builder.deleteCharAt(builder.length()-1);
					
					Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_SHORT).show();
				}
				notifyDataSetChanged();
			}
		});
		
		int stringID = answer.isSolution() ? R.string.correct_answer : R.string.wrong_answer; 
		buttonWrongCorrectAnswer.setText(layoutItem.getContext().getString(stringID));
		editTextAnswer.setText(answer.getText());

		return layoutItem;
	}

	/**
	 * @return the editQuestionActivity
	 */
	public EditQuestionActivity getEditQuestionActivity() {
		return this.mEditQuestionActivity;
	}

}
