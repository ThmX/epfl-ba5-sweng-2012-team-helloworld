package epfl.sweng.quizratings;

/**
 * Exception raised when an exception is thrown during the generation of the
 * QuizQuestion
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class QuizRatingException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create an exception with message msg
	 */
	public QuizRatingException(String msg) {
		super(msg);
	}
	
	/**
	 * Create an exception with t as cause
	 */
	public QuizRatingException(Throwable t) {
		super(t);
	}

}
