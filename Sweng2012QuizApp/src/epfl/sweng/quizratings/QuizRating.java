package epfl.sweng.quizratings;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.JSONSerializer;

/**
 * 
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public enum QuizRating implements JSONSerializer {
	
	none, like, dislike, incorrect;
	
	public static QuizRating fromJSON(JSONObject json) throws QuizRatingException  {
		if (json.has("message")) {
			try {
				throw new QuizRatingException(json.getString("message"));
			} catch (JSONException e) {
				throw new QuizRatingException(e);
			}
		}
		
		try {
			return QuizRating.valueOf(json.getString("verdict"));
		} catch (JSONException e) {
			throw new QuizRatingException(e);
			
		} catch (IllegalArgumentException e) {
			throw new QuizRatingException(e);
		}
	}
	
	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("verdict", name());
		
		return json;
	}
}
