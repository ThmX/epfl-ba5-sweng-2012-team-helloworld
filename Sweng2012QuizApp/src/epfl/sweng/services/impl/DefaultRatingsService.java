package epfl.sweng.services.impl;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.quizratings.QuizRatingException;
import epfl.sweng.quizratings.QuizRatingStats;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;
import epfl.sweng.services.SwengResponseHandler;

/**
 * Class to fetch the question rating on the server.
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class DefaultRatingsService extends AbstractImplService implements RatingsService {

	private static final String RATING = "/rating";
	private static final String RATINGS = "/ratings";

	public DefaultRatingsService(String baseUrl, SessionManager sessionManager) {
		super(baseUrl, sessionManager);
	}

	/* (non-Javadoc)
	 * @see epfl.sweng.services.RatingsService#ratings(int)
	 */
	@Override
	public QuizRatingStats ratings(int id) throws ServiceException {
		HttpGet ratingsHttp = new HttpGet(getBaseUrl() + SEPARATOR + id + RATINGS);

		ResponseHandler<String> handler = new SwengResponseHandler(SwengResponseHandler.HTTP_OK);

		QuizRatingStats stats = null;
		try {
			String jsonString = execute(ratingsHttp, handler);

			stats = new QuizRatingStats(new JSONObject(jsonString));

		} catch (QuizRatingException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}

		return stats;
	}

	/* (non-Javadoc)
	 * @see epfl.sweng.services.RatingsService#rating(int)
	 */
	@Override
	public QuizRating rating(int id) throws ServiceException {
		HttpGet ratingHttp = new HttpGet(getBaseUrl() + SEPARATOR + id + RATING);

		ResponseHandler<String> handler = new SwengResponseHandler(
			SwengResponseHandler.HTTP_OK,
			SwengResponseHandler.HTTP_NO_CONTENT
		);

		QuizRating rating = null;
		try {
			String jsonString = execute(ratingHttp, handler);
			if (jsonString == null) {
				rating = QuizRating.none;
			} else {				
				rating = QuizRating.fromJSON(new JSONObject(jsonString));
			}

		} catch (QuizRatingException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}

		return rating;
	}

	/* (non-Javadoc)
	 * @see epfl.sweng.services.RatingsService#rate(int, epfl.sweng.quizratings.QuizRating)
	 */
	@Override
	public void rate(int id, QuizRating rating) throws ServiceException {
		HttpPost rateHttp = new HttpPost(getBaseUrl() + SEPARATOR + id + RATING);

		ResponseHandler<String> handler = new SwengResponseHandler(
			SwengResponseHandler.HTTP_OK,
			SwengResponseHandler.HTTP_CREATED
		);

		try {
			rateHttp.setEntity(new StringEntity(rating.toJSON().toString()));
			rateHttp.setHeader("Content-type", "application/json");

			String reponse = execute(rateHttp, handler);
			
			if ((reponse != null) && !reponse.isEmpty()) {
				throw new ServiceException(new QuizRatingException("Should be either null or empty."));
			}

		} catch (IOException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
			
		} catch (JSONException e) {
			getSessionManager().setOffline(true);
			throw new ServiceException(e);
		}
	}

}
