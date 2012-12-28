package epfl.sweng.quizquestions;

import java.io.Serializable;

/**
 * This model is a small container used to hold a mutable answer.
 * 
 * @version 1.1.1 - 2012.10.15 - Builder usable
 * @version 1.1.0 - 2012.10.14 - Renamed, made mutable
 * @version 1.0.0 - 2012.09.30 - Initial version
 */
public class QuizAnswerBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	private QuizQuestionBuilder mBuilder;

	// Internal storage. May be null.
	private String mText;
	private boolean mSolution;

	/**
	 * Create an empty mutable answer with its builder.
	 */
	QuizAnswerBuilder(QuizQuestionBuilder builder) {
		this.mBuilder = builder;
	}

	/**
	 * Create a mutable answer from an immutable one.
	 */
	QuizAnswerBuilder(QuizQuestionBuilder builder, QuizAnswer answer) {
		this(builder);
		this.mText = answer.getText();
		this.mSolution = answer.isSolution();
	}

	/*
	 * Getters & Setters
	 */

	/**
	 * @return the answer content
	 */
	public String getText() {
		return this.mText;
	}

	/**
	 * Set a new answer content.
	 */
	public void setText(String text) {
		this.mText = text;
	}

	/**
	 * @return whether this is the correct solution.
	 */
	public boolean isSolution() {
		return this.mSolution;
	}

	/**
	 * @param solution
	 */
	public void setSolution(boolean solution) {
		this.mSolution = solution;
	}

	/**
	 * Switch the solution
	 */
	public void switchSolution() {
		this.mSolution = !this.mSolution;
		if (this.mSolution) {
			this.mBuilder.setSolution(this);
		}
	}

	/*
	 * Builder used methods
	 */

	/**
	 * Remove the question from their builder
	 * 
	 * @throws QuizBuildingException
	 */
	public void remove() throws QuizBuildingException {
		if (this.mBuilder != null) {
			this.mBuilder.removeAnswer(this);

			// Prevent from trying to remove twice (we never know...)
			this.mBuilder = null;
		}
	}

	/**
	 * @return the generated immutable QuizAnswer
	 */
	public QuizAnswer generate() {
		return new QuizAnswer(this.mText, this.mSolution);
	}

	/*
	 * Eclipse auto-generated Object methods
	 */

	@Override
	public String toString() {
		return "QuizAnswerBuilder [mText=" + this.mText + ", mSolution=" + this.mSolution + "]";
	}

}
