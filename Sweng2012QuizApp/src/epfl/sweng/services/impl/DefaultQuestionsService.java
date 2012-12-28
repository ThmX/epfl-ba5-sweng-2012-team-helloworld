package epfl.sweng.services.impl;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.quizquestions.QuizMessageException;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.services.QuestionsService;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;
import epfl.sweng.services.SwengResponseHandler;

/**
 * Class to fetch question on the server.
 * 
 * @version
 */
public class DefaultQuestionsService extends AbstractImplService implements QuestionsService {

	private static final String RANDOM = "/random";

	public DefaultQuestionsService(String baseUrl, SessionManager sessionManager) {
		super(baseUrl, sessionManager);
	}

	/* (non-Javadoc)
	 * @see epfl.sweng.services.QuestionsService#random()
	 */
	@Override
	public QuizQuestion random() throws ServiceException {
		QuizQuestion randomQuestion = null;

		HttpGet randomQuestionHttp = new HttpGet(getBaseUrl() + RANDOM);
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			String questionString = execute(randomQuestionHttp, handler);
			randomQuestion = new QuizQuestion(questionString);
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (QuizMessageException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}

		return randomQuestion;
	}
	
	/* (non-Javadoc)
	 * @see epfl.sweng.services.QuestionsService#get(int)
	 */
	@Override
	public QuizQuestion get(int id) throws ServiceException {
		QuizQuestion question = null;

		HttpGet questionHttp = new HttpGet(getBaseUrl() + SEPARATOR + id);
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);
		
		try {
			String questionString = execute(questionHttp, handler);
			question = new QuizQuestion(questionString);
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (QuizMessageException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}

		return question;
	}
	
	/* (non-Javadoc)
	 * @see epfl.sweng.services.QuestionsService#delete(int)
	 */
	@Override
	public void delete(int id) throws ServiceException {
		HttpDelete deleteQuestionHttp = new HttpDelete(getBaseUrl() + SEPARATOR + id);
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_NO_CONTENT);
		
		execute(deleteQuestionHttp, handler);
	}
	
	/* (non-Javadoc)
	 * @see epfl.sweng.services.QuestionsService#post(epfl.sweng.quizquestions.QuizQuestion)
	 */
	@Override
	public QuizQuestion post(QuizQuestion newQuestion) throws ServiceException {
		QuizQuestion question = null;

		HttpPost questionHttp = new HttpPost(getBaseUrl());
		
		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_CREATED);
		
		try {
			JSONObject json = newQuestion.toJSON();
			json.remove("id");
			json.remove("owner");
			questionHttp.setEntity(new StringEntity(json.toString()));
			questionHttp.setHeader("Content-type", "application/json");
			
			String response = execute(questionHttp, handler);
			
			question = new QuizQuestion(response);
		} catch (IOException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (QuizMessageException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}

		return question;
	}

}
