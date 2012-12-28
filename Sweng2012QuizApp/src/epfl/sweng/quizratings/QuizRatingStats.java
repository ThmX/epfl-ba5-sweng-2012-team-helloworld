package epfl.sweng.quizratings;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.JSONSerializer;

/**
 * Encapsulate the stats from ratings
 * 
 * @version 4.0.0 - 2012.11.15 - Initial version
 */
public class QuizRatingStats implements JSONSerializer {

	private int mLikes;
	private int mDislikes;
	private int mIncorrects;
	
	public QuizRatingStats(int likes, int dislikes, int incorrects) {
		this.mLikes = likes;
		this.mDislikes = dislikes;
		this.mIncorrects = incorrects;
	}
	
	public QuizRatingStats(JSONObject json) throws QuizRatingException {
		if (json.has("message")) {
			try {
				throw new QuizRatingException(json.getString("message"));
			} catch (JSONException e) {
				throw new QuizRatingException(e);
			}
		}
		
		try {
			this.mLikes = json.getInt("likeCount");
			this.mDislikes = json.getInt("dislikeCount");
			this.mIncorrects = json.getInt("incorrectCount");
			
			if ((this.mLikes < 0) || (this.mDislikes < 0) || (this.mIncorrects < 0)) {
				throw new QuizRatingException("Can't be < 0");
			}
		} catch (JSONException e) {
			throw new QuizRatingException(e);
		}
	}
	
	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("likeCount", this.mLikes);
		json.put("dislikeCount", this.mDislikes);
		json.put("incorrectCount", this.mIncorrects);
		
		return json;
	}
	
	/**
	 * @return the likes
	 */
	public int getLikes() {
		return this.mLikes;
	}
	
	/**
	 * @return the dislikes
	 */
	public int getDislikes() {
		return this.mDislikes;
	}
	
	/**
	 * @return the incorrects
	 */
	public int getIncorrects() {
		return this.mIncorrects;
	}
	
}
