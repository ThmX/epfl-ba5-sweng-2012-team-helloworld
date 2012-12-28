package epfl.sweng.quizquestions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builder allowing to create a question
 * 
 * @version 1.0.1 - 2012.10.19 - Better audit
 * @version 1.0.0 - 2012.10.15 - Initial version
 */
public class QuizQuestionBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mText;
	private List<QuizAnswerBuilder> mAnswersBuilder;
	private Set<String> mTags;
	private int mID;
	private String mOwner;

	/**
	 * Create an empty new question.
	 */
	public QuizQuestionBuilder() {
		this.mText = null;
		this.mAnswersBuilder = new ArrayList<QuizAnswerBuilder>();
		this.mAnswersBuilder.add(new QuizAnswerBuilder(this));
		this.mTags = new HashSet<String>();
		this.mID = -1;
		this.mOwner = null;
	}

	/**
	 * Create a new question according to specified non-null arguments.
	 */
	public QuizQuestionBuilder(QuizQuestion question) {
		super();
		this.mID = question.getID();
		this.mText = question.getText();
		this.mAnswersBuilder = new ArrayList<QuizAnswerBuilder>();
		for (QuizAnswer qa : question.getAnswers()) {
			this.mAnswersBuilder.add(new QuizAnswerBuilder(this, qa));
		}
		this.mTags = new HashSet<String>(question.getTags());
		this.mOwner = question.getOwner();
	}

	/**
	 * @return the generated question
	 */
	public QuizQuestion generate() {
		int solutionIndex = -1;
		List<String> answers = new ArrayList<String>();
		for (int i = 0; i < this.mAnswersBuilder.size(); i++) {
			QuizAnswerBuilder mqa = this.mAnswersBuilder.get(i);
			answers.add(mqa.getText());
			if (mqa.isSolution()) {
				solutionIndex = i;
			}
		}
		return new QuizQuestion(this.mText, answers, solutionIndex, getTags(), this.mID, this.mOwner);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.mText;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.mText = text;
	}

	/**
	 * Create a new Mutable Answer and allow the user to edit it
	 * 
	 * @return The new answer
	 */
	public QuizAnswerBuilder newAnswer() throws QuizBuildingException {
		if (this.mAnswersBuilder.size() >= QuizQuestion.ANSWERS_MAX) {
			throw new QuizBuildingException("There are already the max number of answer.");
		}
		QuizAnswerBuilder answer = new QuizAnswerBuilder(this);
		this.mAnswersBuilder.add(answer);
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to remove from the builder
	 * @return if the answer was remove
	 * @throws QuizBuildingException
	 */
	public boolean removeAnswer(QuizAnswerBuilder answer) throws QuizBuildingException {
		if (this.mAnswersBuilder.isEmpty()) {
			throw new QuizBuildingException("There are no answer to remove.");
		}

		return this.mAnswersBuilder.remove(answer);
	}

	/**
	 * @return the answer according to specified index
	 */
	public QuizAnswerBuilder getAnswer(int position) {
		return this.mAnswersBuilder.get(position);
	}

	/**
	 * @return how many answers are stored
	 */
	public int getAnswerCount() {
		return this.mAnswersBuilder.size();
	}

	/**
	 * @param tag
	 *            the tag to add to the question
	 */
	public void addTag(String tag) {
		this.mTags.add(tag);
	}

	/**
	 * @param tag
	 *            the tag to remove from the question
	 * @return if the tag was remove
	 */
	public boolean removeTag(String tag) {
		return this.mTags.remove(tag);
	}

	/**
	 * @param tags
	 *            the tags to add to the question
	 */
	public void addTags(String tags) {
		// Extract tags from regex
		Pattern pattern = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tags);

		// No match means the report is unknown.
		while (matcher.find()) {
			addTag(matcher.group());
		}
	}

	/**
	 * @param tags
	 *            the tags to add to the question
	 */
	public void setTags(String tags) {
		this.mTags = new HashSet<String>();
		addTags(tags);
	}

	/**
	 * @return the tags
	 */
	public Set<String> getTags() {
		return new HashSet<String>(this.mTags);
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return this.mID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(int iD) {
		this.mID = iD <= 0 ? -1 : iD;
	}

	/**
	 * @return the author/owner name
	 */
	public String getOwner() {
		return this.mOwner;
	}

	/**
	 * @param owner
	 *            the author/owner name to set
	 */
	public void setOwner(String owner) {
		this.mOwner = owner;
	}

	@Override
	public String toString() {
		return "QuizQuestionBuilder [mText=" + this.mText + ", mAnswersBuilder=" + this.mAnswersBuilder + ", mTags="
				+ this.mTags + ", mID=" + this.mID + "]";
	}

	public void setSolution(QuizAnswerBuilder quizAnswerBuilder) {
		for (QuizAnswerBuilder qab : this.mAnswersBuilder) {
			if (!qab.equals(quizAnswerBuilder)) {
				qab.setSolution(false);
			}
		}
		quizAnswerBuilder.setSolution(true);
	}

}
