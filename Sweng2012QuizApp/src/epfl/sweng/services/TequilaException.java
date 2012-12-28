package epfl.sweng.services;

/**
 * Exception thrown when an error has occured during the authentication
 */
public class TequilaException extends Exception {

	private static final long serialVersionUID = 1L;

	public TequilaException(Throwable cause) {
		 super(cause);
	}
}
