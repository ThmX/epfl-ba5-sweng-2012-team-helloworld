package epfl.sweng.quizquestions;

/**
 * Exception raised when a message is provided instead of question content (in
 * JSON structure).
 * 
 * @version 1.0.0 - 2012.10.19 - Initial version
 */
public class QuizMessageException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new message exception with specified text.
	 */
	public QuizMessageException(String message) {
		super(message);
	}
}
