package epfl.sweng.servercomm.search;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;

import epfl.sweng.quizquestions.QuizMessageException;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.services.SwengResponseHandler;

/**
 * 
 * @author thmx
 *
 */
public class DefaultQuestionSearchCommunication implements QuestionSearchCommunication {
	
	private static final String OWNEDBY_URL = "https://sweng-quiz.appspot.com/quizquestions/ownedby/";
	private static final String TAGGED_URL = "https://sweng-quiz.appspot.com/quizquestions/tagged/";
	
	public boolean validateParameter(String param) {
		
		if (param == null) {
			return false;
		}
		
		if (param.isEmpty()) {
			return false;
		}
		
		final int maxLength = 20;
		if (param.length() > maxLength) {
			return false;
		}
		
		for (char c: param.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public List<QuizQuestion> getQuestionsByOwner(String owner) throws CommunicationException {
		
		if (!validateParameter(owner)) {
			throw new IllegalArgumentException("Owner shouldn't be null or longer than 20.");
		}
		
		HttpGet ownedByHttp = new HttpGet(OWNEDBY_URL + owner);

		// TODO Authentication ???
		//addAuthentication(questionHttp);
		
		List<QuizQuestion> questions = fetchQuestionsFromHttp(ownedByHttp);
		
		for (QuizQuestion q : questions) {
			if (!q.getOwner().equals(owner)) {
				throw new CommunicationException("Ownder should be " + owner + " not " + q.getOwner());
			}
		}
		
		return questions;
	}
	
	@Override
	public List<QuizQuestion> getQuestionsByTag(String tag) throws CommunicationException {
		
		if (!validateParameter(tag)) {
			throw new IllegalArgumentException("Tag shouldn't be null or longer than 20.");
		}
		
		HttpGet taggedHttp = new HttpGet(TAGGED_URL + tag);

		// TODO Authentication ???
		//addAuthentication(questionHttp);
		
		List<QuizQuestion> questions = fetchQuestionsFromHttp(taggedHttp);
		
		for (QuizQuestion q : questions) {
			if (!q.getTags().contains(tag)) {
				throw new CommunicationException("Question (" + q.getID() + ") should contains tag: " + tag);
			}
		}
		
		return questions;
	}


	private List<QuizQuestion> fetchQuestionsFromHttp(HttpGet ownedByHttp) throws CommunicationException {
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		List<QuizQuestion> questions = new LinkedList<QuizQuestion>();
		
		try {
			String questionsString = SwengHttpClientFactory.getInstance().execute(ownedByHttp, handler);
				
			JSONArray array = new JSONArray(questionsString);
				
			int lenght = array.length();
			for (int i = 0; i < lenght; i++) {
				QuizQuestion q = new QuizQuestion(array.getJSONObject(i));
				
				int audit = q.auditErrors(0);
				if (audit > 0) {
					throw new CommunicationException("Audit of " + audit);
				}
				
				questions.add(q);
			}
			
			if (questions.isEmpty()) {
				throw new CommunicationException("Should not be empty");
			}
			
		} catch (HttpResponseException e) {
			if (e.getStatusCode() == SwengResponseHandler.HTTP_NOT_FOUND) {
				return questions;
			}
			throw new CommunicationException(e);
			
		} catch (IOException e) {
			throw new CommunicationException(e);
		
		} catch (JSONException e) {
			throw new CommunicationException(e);
					
		} catch (QuizMessageException e) {
			throw new CommunicationException(e);
		}
		return questions;
	}

}
