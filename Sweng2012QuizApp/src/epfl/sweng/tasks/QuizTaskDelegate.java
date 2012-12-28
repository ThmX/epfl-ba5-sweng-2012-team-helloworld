package epfl.sweng.tasks;

import epfl.sweng.quizzes.Quiz;

/**
 * Callback used when quiz fetch has ended.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public interface QuizTaskDelegate {
	void setQuiz(Quiz quiz);
	void setScore(Double score);
}
