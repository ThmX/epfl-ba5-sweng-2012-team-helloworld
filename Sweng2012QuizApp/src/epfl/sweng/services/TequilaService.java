package epfl.sweng.services;

/**
 * 
 * @author thmx
 *
 */
public interface TequilaService {

	/**
	 * Current state of the authentication
	 */
	String authenticate(String username, String password) throws TequilaException;

}