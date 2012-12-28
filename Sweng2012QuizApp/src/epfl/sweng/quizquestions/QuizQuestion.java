package epfl.sweng.quizquestions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.JSONSerializer;

/**
 * This model is a container for a quiz question.
 * 
 * @version 1.1.2 - 2012.10.18 - Made immutable again
 * @version 1.1.1 - 2012.10.15 - Serializable
 * @version 1.1.0 - 2012.10.14 - Renamed, moved <code>fromJSON</code> to
 *          constructor, added audit function, made mutable
 * @version 1.0.0 - 2012.09.30 - Initial version
 */
public class QuizQuestion implements Serializable, JSONSerializer {

	private static final long serialVersionUID = 1L;

	// Internal constants.
	public static final int TEXT_MAX_LENGTH = 500;
	public static final int NAME_MAX_LENGTH = 20;
	public static final int ANSWERS_MIN = 2;
	public static final int ANSWERS_MAX = 10;

	// Internal storage. May be null.
	private String mText;
	private List<QuizAnswer> mAnswers;
	private int mSolutionIndex;
	private Set<String> mTags;
	private int mID;
	private String mOwner;

	/**
	 * Create a new question according to specified (possibly null) arguments.
	 */
	public QuizQuestion(String text, List<String> answers, int solutionIndex, Set<String> tags, int iD, String owner) {
		super();
		this.mID = iD <= 0 ? -1 : iD;
		this.mText = text;
		if (answers == null) {
			this.mAnswers = null;
		} else {
			this.mAnswers = new ArrayList<QuizAnswer>(answers.size());
			for (int i = 0; i < answers.size(); i++) {
				this.mAnswers.add(new QuizAnswer(answers.get(i), i == solutionIndex));
			}
		}
		this.mSolutionIndex = solutionIndex;
		if (tags == null) {
			this.mTags = null;
		} else {
			this.mTags = new HashSet<String>(tags);
		}
		this.mOwner = owner;
	}

	// TODO add constructor with list of answers?

	/**
	 * Create a new question according to given <code>JSONObject</code>. Missing
	 * parameters are set to <code>null</code> (<code>-1</code> for integers).
	 * 
	 * @throws QuizMessageException
	 *             if specified JSON representation is considered harmful (i.e.
	 *             it contains a field message)
	 */
	public QuizQuestion(JSONObject json) throws QuizMessageException {
		// Check for error message
		String message = json.optString("message", null);
		if (message != null) {
			throw new QuizMessageException(message);
		}
		// Load ID
		this.mID = json.optInt("id", -1);
		if (this.mID <= 0) {
			this.mID = -1;
		}
		// Load answers
		this.mText = json.optString("question", null);
		this.mSolutionIndex = json.optInt("solutionIndex", -1);
		JSONArray answersArr = json.optJSONArray("answers");
		if (answersArr == null) {
			this.mAnswers = null;
		} else {
			this.mAnswers = new ArrayList<QuizAnswer>(answersArr.length());
			for (int i = 0; i < answersArr.length(); ++i) {
				QuizAnswer answer = new QuizAnswer(answersArr.optString(i, null), i == this.mSolutionIndex);
				this.mAnswers.add(answer);
			}
		}
		// Load tags
		JSONArray tagsArr = json.optJSONArray("tags");
		if (tagsArr == null) {
			this.mTags = null;
		} else {
			this.mTags = new HashSet<String>(tagsArr.length());
			for (int i = 0; i < tagsArr.length(); ++i) {
				this.mTags.add(tagsArr.optString(i, null));
			}
		}
		// Load owner
		this.mOwner = json.optString("owner", null);
	}

	/**
	 * Create a new question according to given <code>JSONObject</code>. Missing
	 * parameters are set to <code>null</code> (<code>-1</code> for integers).
	 * 
	 * @throws JSONException
	 *             if specified string isn't a valid JSON encoded object
	 * @throws QuizMessageException
	 *             if specified JSON representation is considered harmful (i.e.
	 *             it contains a field message)
	 */
	public QuizQuestion(String json) throws JSONException, QuizMessageException {
		this(new JSONObject(json));
	}

	/**
	 * @return a JSON representation of this question
	 */
	@Override
	public JSONObject toJSON() throws JSONException {
		// AFAIK, a JSONException will never be thrown...
		JSONObject obj = new JSONObject();
		if (getID() != -1) {
			obj.put("id", getID());
		}
		if (getText() != null) {
			obj.put("question", getText());
		}
		if (getAnswers() != null) {
			JSONArray answersArr = new JSONArray();
			for (QuizAnswer a : getAnswers()) {
				answersArr.put(a.getText());
			}
			obj.put("answers", answersArr);
		}
		if (getSolutionIndex() != -1) {
			obj.put("solutionIndex", getSolutionIndex());
		}
		if (getTags() != null) {
			obj.put("tags", new JSONArray(getTags()));
		}
		if (getOwner() != null) {
			obj.put("owner", getOwner());
		}
		return obj;
	}

	public Set<String> auditErrorsAsText(int depth) {
		Set<String> result = new HashSet<String>();
		// Text isn't blank and has at least 500 characters.
		if (getText() == null) {
			result.add("Text is unspecified!");
		} else {
			String textError = validateText(getText(), TEXT_MAX_LENGTH, "Text");
			if (textError != null) {
				result.add(textError);
			}
		}
		// There are 2 <= n <= 10 answers. Solution index isn't out of bounds.
		int solutionIndex = getSolutionIndex();
		List<QuizAnswer> answers = getAnswers();
		if (answers == null) {
			result.add("Answers is unspecified!");
		} else if (answers.size() < ANSWERS_MIN) {
			result.add("At least " + ANSWERS_MIN + " answers must be specified!");
		} else if (answers.size() > ANSWERS_MAX) {
			result.add("At most " + ANSWERS_MAX + " answers must be specified!");
		} else {
			if (solutionIndex < 0 || solutionIndex >= answers.size()) {
				result.add("Solution index is out of bounds!");
			}
			for (int i = 0; i < answers.size(); ++i) {
				QuizAnswer answer = answers.get(i);
				String answerError = validateText(answer.getText(), TEXT_MAX_LENGTH, "Answer " + i);
				if (answerError != null) {
					result.add(answerError);
				}
				if (answer.isSolution() && i != solutionIndex) {
					result.add("Answer " + (i + 1) + " cannot be marked as solution, since solution index is "
							+ solutionIndex + "!");
				} else if (!answer.isSolution() && i == solutionIndex) {
					result.add("Answer " + (i + 1) + " must be marked as solution!");
				}
			}
		}
		// Tags must be no longer than 20 alphanumeric characters.
		Set<String> tags = getTags();
		if (tags == null) {
			result.add("Tags is unspecified!");
		} else {
			for (String tag : tags) {
				String tagError = validateName(tag, NAME_MAX_LENGTH, "Tag");
				if (tagError != null) {
					result.add(tagError);
				}
			}
		}
		// Owner is a string no longer than 20 alphanumeric characters
		if (getOwner() == null) {
			result.add("Owner is unspecified!");
		} else {
			String ownerError = validateName(getOwner(), NAME_MAX_LENGTH, "Owner");
			if (ownerError != null) {
				result.add(ownerError);
			}
		}
		// id > 0
		if (getID() <= 0) {
			result.add("ID must be a positive integer!");
		}
		return result;
	}

	/**
	 * @return a text describing why this text is invalid. Null if correct.
	 */
	private String validateText(String str, int max, String name) {
		if (str != null) {
			if (str.length() > max) {
				return name + " is too long! (max " + max + " chars)";
			}
			for (int i = 0; i < str.length(); ++i) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return null;
				}
			}
		}
		return name + " cannot be a blank string!";
	}

	/**
	 * @return a text describing why this name is invalid. Null if correct.
	 */
	private String validateName(String str, int max, String name) {
		if (str == null || str.length() == 0) {
			return name + " cannot be empty!";
		}
		if (str.length() > max) {
			return name + " is too long! (max " + max + " chars)";
		}
		for (int i = 0; i < str.length(); ++i) {
			if (!Character.isLetterOrDigit(str.charAt(i))) {
				return name + " must only contain alphanumeric characters!";
			}
		}
		return null;
	}

	/**
	 * Returns the number of rep invariant violations
	 */
	public int auditErrors(int depth) {
		return auditErrorsAsText(depth).size();
	}

	/**
	 * @return the statement
	 */
	public String getText() {
		return this.mText;
	}

	/**
	 * @return a read-only list of available answers
	 */
	public List<QuizAnswer> getAnswers() {
		return this.mAnswers == null ? null : Collections.unmodifiableList(this.mAnswers);
	}

	/**
	 * @return the solution index, according to answers list
	 */
	public int getSolutionIndex() {
		return this.mSolutionIndex;
	}

	/**
	 * @return a read-only set of associated tags
	 */
	public Set<String> getTags() {
		return this.mTags == null ? null : Collections.unmodifiableSet(this.mTags);
	}

	/**
	 * @return the unique identifier
	 */
	public int getID() {
		return this.mID;
	}

	/**
	 * @return the author/owner name
	 */
	public String getOwner() {
		return this.mOwner;
	}

	@Override
	public String toString() {
		return "Question [mID=" + getID() + ", mText=" + getText() + ", mAnswers=" + getAnswers() + ", mSolutionIndex="
				+ getSolutionIndex() + ", mTags=" + getTags() + ", mOwner=" + getOwner() + "]";
	}

	@Override
	public int hashCode() {
		return getID();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO compare other fields?
		return this == obj || (obj != null && getClass() == obj.getClass() && getID() == ((QuizQuestion) obj).getID());
	}
}
