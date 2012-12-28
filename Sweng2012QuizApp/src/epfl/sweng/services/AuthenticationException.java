package epfl.sweng.services;

/**
 * Exception thrown when an error has occured during the authentication
 */
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		 super(message);
	}
}
