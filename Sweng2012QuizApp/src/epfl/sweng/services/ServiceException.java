package epfl.sweng.services;

/**
 * Custom Exception class for the QuestionsFetcher class
 */
public class ServiceException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ServiceException(Throwable cause) {
		 super(cause);
	}
}
