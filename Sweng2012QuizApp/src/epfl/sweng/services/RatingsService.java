package epfl.sweng.services;

import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.quizratings.QuizRatingStats;

/**
 * 
 * @author thmx
 *
 */
public interface RatingsService {

	QuizRatingStats ratings(int id) throws ServiceException;

	QuizRating rating(int id) throws ServiceException;

	void rate(int id, QuizRating rating) throws ServiceException;

}