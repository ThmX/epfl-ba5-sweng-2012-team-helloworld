package epfl.sweng.tasks;

import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.quizratings.QuizRatingStats;

/**
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public interface RatingTaskDelegate {
	void setRating(QuizRating rating);
	void setRatings(QuizRatingStats stats);
	void rate(boolean error);
}