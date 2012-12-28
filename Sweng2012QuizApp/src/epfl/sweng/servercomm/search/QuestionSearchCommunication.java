package epfl.sweng.servercomm.search;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;

/** 
 *  Retrieve questions from the server, by specifying either an owner or a tag
 *  name.
 *  
 *  DO NOT CHANGE THIS FILE. ANY CHANGES WILL CAUSE OUR TESTS TO FAIL AND YOU
 *  WILL LOSE ALL THE POINTS.
 *  
 */
public interface QuestionSearchCommunication {
    List<QuizQuestion> getQuestionsByOwner(String owner) throws CommunicationException;
    List<QuizQuestion> getQuestionsByTag(String tag) throws CommunicationException;
}
