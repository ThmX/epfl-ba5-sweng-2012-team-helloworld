package epfl.sweng.quizquestions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Exception raised when an exception is thrown during the generation of the
 * QuizQuestion
 * 
 * @version 1.0.1 - 2012.10.19 - Added audits
 * @version 1.0.0 - 2012.10.15 - Initial version
 */
public class QuizBuildingException extends Exception {

	private static final long serialVersionUID = 1L;

	private Set<String> mAudits;

	/**
	 * Create a collection with specified audit errors.
	 */
	public QuizBuildingException(Set<String> audits) {
		super("Audit failed with " + audits.size() + " errors!");
		this.mAudits = new HashSet<String>(audits);
	}

	/**
	 * Create an exception with only one audit error
	 */
	public QuizBuildingException(String msg) {
		this(Collections.singleton(msg));
	}

	/**
	 * @return an unmodifiable set of audit errors
	 */
	public Set<String> getAudits() {
		return Collections.unmodifiableSet(this.mAudits);
	}

}
