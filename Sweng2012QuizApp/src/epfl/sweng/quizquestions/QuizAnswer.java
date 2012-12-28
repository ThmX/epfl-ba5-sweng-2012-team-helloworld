package epfl.sweng.quizquestions;

import java.io.Serializable;

/**
 * This model is a small container used to hold an answer.
 * 
 * @version 1.1.2 - 2012.10.19 - Made immutable again
 * @version 1.1.1 - 2012.10.15 - Serializable
 * @version 1.1.0 - 2012.10.14 - Renamed, made mutable
 * @version 1.0.0 - 2012.09.30 - Initial version
 */
public class QuizAnswer implements Serializable {

	private static final long serialVersionUID = 1L;

	// Internal storage. May be null.
	private String mText;
	private boolean mSolution;

	/**
	 * Create a new answer according to specified non-null arguments.
	 */
	public QuizAnswer(String text, boolean solution) {
		super();
		this.mText = text;
		this.mSolution = solution;
	}

	/**
	 * @return the answer content
	 */
	public String getText() {
		return this.mText;
	}

	/**
	 * @return whether this is the correct solution.
	 */
	public boolean isSolution() {
		return this.mSolution;
	}

	@Override
	public String toString() {
		return "Answer [mText=" + getText() + ", mSolution=" + isSolution() + "]";
	}

	@Override
	public int hashCode() {
		return getText() == null ? 2 : getText().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && getClass() == obj.getClass()) {
			String str = getText();
			String oStr = ((QuizAnswer) obj).getText();
			if (str == null) {
				return oStr == null;
			}
			return str.equals(oStr);
		}
		return false;
	}

}
