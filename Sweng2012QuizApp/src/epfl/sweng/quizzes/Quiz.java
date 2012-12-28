package epfl.sweng.quizzes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.JSONSerializer;
import epfl.sweng.quizquestions.QuizAnswer;
import epfl.sweng.quizquestions.QuizMessageException;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * This model is a small container used to hold a quiz.
 * 
 * @version 5.0.0 - 2012.11.27 - Initial version
 */
public class Quiz implements Serializable, JSONSerializer {

	private static final long serialVersionUID = 1L;

	/**
	 * Wrap a Question with the user answer
	 */
	private static class QuestionWrapper implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private QuizQuestion mQuestion;
		private Integer mUserAnswer;

		public QuestionWrapper(QuizQuestion question) {
			super();
			this.mQuestion = question;
		}

		/**
		 * @return the question
		 */
		public QuizQuestion getQuestion() {
			return this.mQuestion;
		}

		/**
		 * @param question
		 *            the question to set
		 */
		public void setQuestion(QuizQuestion question) {
			this.mQuestion = question;
		}

		/**
		 * @return the answer
		 */
		public Integer getUserAnswer() {
			return this.mUserAnswer;
		}

		/**
		 * @param answer
		 *            the answer to set
		 */
		public void setUserAnswer(Integer answer) {
			this.mUserAnswer = answer;
		}
	}

	private int mID;
	private String mTitle;
	private List<QuestionWrapper> mWrappers;

	/**
	 * Create a new Quiz from given JSON representation.
	 * 
	 * @throws QuizMessageException
	 *             if this JSON is a message.
	 * @throws QuizException
	 *             if this JSON isn't a valid Quiz.
	 */
	public Quiz(JSONObject json) throws QuizMessageException, QuizException {
		if (json.has("message")) {
			try {
				throw new QuizMessageException(json.getString("message"));
			} catch (JSONException e) {
				throw new QuizException(e);
			}
		}

		try {
			this.mID = json.getInt("id");
			this.mTitle = json.getString("title");
		} catch (JSONException e) {
			throw new QuizException(e);
		}

		try {
			if (json.has("questions")) {
				this.mWrappers = new ArrayList<QuestionWrapper>();

				JSONArray array = json.getJSONArray("questions");

				int lenght = array.length();
				for (int i = 0; i < lenght; i++) {
					QuizQuestion q = new QuizQuestion(array.getJSONObject(i));
					this.mWrappers.add(new QuestionWrapper(q));
				}
			}
		} catch (JSONException e) {
			throw new QuizException(e);
		}
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return this.mID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.mTitle;
	}

	/**
	 * @return the questions
	 */
	public QuizQuestion getQuestion(int index) {
		return this.mWrappers.get(index).getQuestion();
	}

	/**
	 * Replace existing question by a new version. If no question has given ID,
	 * nothing is done.
	 */
	public void reassignQuestion(int index, QuizQuestion question) {
		QuestionWrapper w = this.mWrappers.get(index);
		w.setQuestion(question);
	}

	/**
	 * @return this quiz as a JSON quiz answer.
	 */
	public JSONObject getAnswerAsJSON() throws QuizException {
		JSONObject json = new JSONObject();

		JSONArray array = new JSONArray();
		for (int i = 0; i < this.mWrappers.size(); ++i) {
			QuestionWrapper w = this.mWrappers.get(i);
			Integer an = w.getUserAnswer();
			if (an == null) {
				array.put(JSONObject.NULL);
			} else {
				array.put(an);
			}
		}

		try {
			json.put("choices", array);
		} catch (JSONException e) {
			throw new QuizException(e);
		}

		return json;
	}

	/**
	 * Set user answer for specified question (null to remove any previous
	 * choice).
	 */
	public void setAnswer(int index, Integer answer) {
		QuestionWrapper w = this.mWrappers.get(index);
		w.setUserAnswer(answer);
	}

	/**
	 * @return user answer for given question (null if unanswered).
	 */
	public Integer getAnswer(int index) {
		try {
			QuestionWrapper w = this.mWrappers.get(index);
			return w.getUserAnswer();
		} catch (IndexOutOfBoundsException e) {
			return null;			
		}
	}

	/**
	 * @return how many questions are defined (-1 if not fetched).
	 */
	public int getCount() {
		return isFetched() ? this.mWrappers.size() : -1;
	}

	/**
	 * @return whether questions of this quiz have been fetched.
	 */
	public boolean isFetched() {
		return this.mWrappers != null;
	}

	@Override
	public JSONObject toJSON() throws JSONException {

		JSONObject obj = new JSONObject();
		
		if (getID() != -1) {
			obj.put("id", getID());
		}
		
		if (getTitle() != null) {
			obj.put("title", getTitle());
		}
		
		if (this.mWrappers != null) {
			
			JSONArray questionWrapperListArray = new JSONArray();
			
			for (QuestionWrapper qw : this.mWrappers) {
				JSONObject questionWrapperListObj = new JSONObject();
				questionWrapperListObj.put("question", qw.getQuestion().getText());
				
				JSONArray answersListArray = new JSONArray();
				for (QuizAnswer qa : qw.getQuestion().getAnswers()) {
					answersListArray.put(qa.getText());
				}
				questionWrapperListObj.put("answers", answersListArray);
				
				questionWrapperListArray.put(questionWrapperListObj);
			}
			
			obj.put("questions", questionWrapperListArray);
		}
		
		return obj;
	}

	@Override
	public int hashCode() {
		return this.mID;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Quiz.class) {
			return false;
		}
		
		return (o == this) || (this.mID == ((Quiz) o).mID);
	}
}
