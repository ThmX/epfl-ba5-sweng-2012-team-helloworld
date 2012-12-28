package epfl.sweng.services;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * 
 * @author thmx
 *
 */
public interface QuestionsService {

	/**
	 * @return a random question fetched from the server
	 * @throws a
	 *             custom ServiceException if something goes wrong with the http
	 *             protocol or JSON
	 */
	QuizQuestion random() throws ServiceException;

	/**
	 * @return a question fetched from the server with his id
	 * @throws a
	 *             custom ServiceException if something goes wrong with the http
	 *             protocol or JSON
	 */
	QuizQuestion get(int id) throws ServiceException;

	/**
	 * @return a question fetched from the server with his id
	 * @throws a
	 *             custom ServiceException if something goes wrong with the http
	 *             protocol or JSON
	 */
	void delete(int id) throws ServiceException;

	/**
	 * @return a question fetched from the server with his id
	 * @throws a
	 *             custom ServiceException if something goes wrong with the http
	 *             protocol or JSON
	 */
	QuizQuestion post(QuizQuestion newQuestion) throws ServiceException;

}