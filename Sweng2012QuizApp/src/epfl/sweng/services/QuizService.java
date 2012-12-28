package epfl.sweng.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.quizquestions.QuizMessageException;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.quizzes.QuizException;
import epfl.sweng.services.impl.AbstractImplService;

/**
 * Class to fetch a quiz on the server
 **/
public class QuizService extends AbstractImplService {

	private static final String SUBMISSION = "submission";
	
	public QuizService(String baseUrl, SessionManager sessionManager) {
		super(baseUrl, sessionManager);
	}
	
	/**
	 * 
	 * @return The list of quizzes available on the server for this user
	 * @throws QuizException
	 * @throws ServiceException
	 **/
	public List<Quiz> get() throws ServiceException {
		
		HttpGet quizzesHttp = new HttpGet(getBaseUrl());
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		List<Quiz> quizzes = new LinkedList<Quiz>();
		
		try {
			String quizzesString = execute(quizzesHttp, handler);
				
			JSONArray array = new JSONArray(quizzesString);
				
			int length = array.length();
			for (int i = 0; i < length; i++) {
				Quiz q = new Quiz(array.getJSONObject(i));
				
				quizzes.add(q);
			}

		} catch (JSONException e) {
			throw new ServiceException(e);
					
		} catch (QuizMessageException e) {
			throw new ServiceException(e);
			
		} catch (QuizException e) {
			throw new ServiceException(e);
		}
		
		return quizzes;
	}
	
	/**
	 * 
	 * @param id
	 * @return the quiz corresponding to the id on the server
	 * @throws QuizException
	 * @throws ServiceException
	 **/
	public Quiz get(int id) throws ServiceException {
		Quiz quiz = null;
		
		HttpGet quizHttp = new HttpGet(getBaseUrl() + SEPARATOR + id);
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			String quizString = execute(quizHttp, handler);
			JSONObject json = new JSONObject(quizString);
			quiz = new Quiz(json);
			
		} catch (JSONException e) {
			throw new ServiceException(e);
					
		} catch (QuizMessageException e) {
			throw new ServiceException(e);
		
		} catch (QuizException e) {
			throw new ServiceException(e);
		}
		
		return quiz;
	}
	
	/**
	 * choicesString of format : ex. : "[ 0, 2, 1, null, 3 ]"
	 * @param choicesString
	 * @param id
	 * @return score achieved, returned by server
	 * @throws QuizException
	 * @throws ServiceException
	 */
	public double post(Quiz quiz) throws ServiceException {

		HttpPost choicesHttp = new HttpPost(getBaseUrl() + SEPARATOR + quiz.getID() + SEPARATOR + SUBMISSION);
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			JSONObject jsonChoices = quiz.getAnswerAsJSON();
			choicesHttp.setEntity(new StringEntity(jsonChoices.toString()));
			choicesHttp.setHeader("Content-type", "application/json");
			
			String response = execute(choicesHttp, handler);
			
			JSONObject jsonResponse = new JSONObject(response);
			return jsonResponse.getDouble("score");
		
		} catch (IOException e) {
			throw new ServiceException(e);
			
		} catch (JSONException e) {
			throw new ServiceException(e);
			
		} catch (QuizException e) {
			throw new ServiceException(e);
		}
	}
}
