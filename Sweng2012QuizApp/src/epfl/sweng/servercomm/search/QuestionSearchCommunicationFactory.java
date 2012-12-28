package epfl.sweng.servercomm.search;

/**
 * Factory class for constructing QuestionSearchCommunication objects.
 *
 * DO NOT CHANGE THIS FILE. ANY CHANGES WILL CAUSE OUR TESTS TO FAIL AND YOU
 * WILL LOSE ALL THE POINTS.
 * 
 */
public class QuestionSearchCommunicationFactory {
    private static QuestionSearchCommunication questionSearch;
    
    public static synchronized QuestionSearchCommunication getInstance() {
        if (questionSearch == null) {
            questionSearch = new DefaultQuestionSearchCommunication();
        }
        return questionSearch;
    }
    
    public static synchronized void setInstance(QuestionSearchCommunication instance) {
        questionSearch = instance;
    }
}
