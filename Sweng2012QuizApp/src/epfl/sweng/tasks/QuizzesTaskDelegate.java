package epfl.sweng.tasks;

import java.util.List;

import epfl.sweng.quizzes.Quiz;

/**
 * Callback used when quizzes fetch has ended.
 * 
 * @version 5.0.0 - 2012.11.28 - Initial version
 */
public interface QuizzesTaskDelegate {
	void setQuizzes(List<Quiz> quizzes);
}
