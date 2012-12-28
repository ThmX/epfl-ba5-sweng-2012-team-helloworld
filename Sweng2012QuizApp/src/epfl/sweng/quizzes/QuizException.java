package epfl.sweng.quizzes;

/**
 * Exception raised when an exception is thrown during the generation of the
 * Quiz
 * 
 * @version 5.0.0 - 2012.11.27 - Initial version
 */
public class QuizException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create an exception with message msg
	 */
	public QuizException(String msg) {
		super(msg);
	}
	
	/**
	 * Create an exception with t as cause
	 */
	public QuizException(Throwable t) {
		super(t);
	}

}
