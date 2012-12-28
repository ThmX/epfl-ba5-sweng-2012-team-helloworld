package epfl.sweng.services.proxy;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import epfl.sweng.quizratings.QuizRating;
import epfl.sweng.quizratings.QuizRatingStats;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;

import android.util.SparseArray;

/**
 * 
 * @author thmx
 *
 */
public class RatingsProxyService extends AbstractProxyService implements RatingsService {
	
	/**
	 * 
	 * @author thmx
	 *
	 */
	private static class RatingWrapper {
		private int mID;
		private QuizRating mRating;
		
		public RatingWrapper(int id, QuizRating rating) {
			this.mID = id;
			this.mRating = rating;
		}
		
		public int getId() {
			return this.mID;
		}
		
		public QuizRating getRating() {
			return this.mRating;
		}
	}
	
	private RatingsService mRatingsService;
	
	private SparseArray<QuizRating> mCacheRating = new SparseArray<QuizRating>();
	private SparseArray<QuizRatingStats> mCacheStats = new SparseArray<QuizRatingStats>();
	
	private Queue<RatingWrapper> mQueueRating = new LinkedBlockingQueue<RatingWrapper>();

	public RatingsProxyService(SessionManager sessionManager, RatingsService ratingsService) {
		super(sessionManager);
		this.mRatingsService = ratingsService;
	}

	@Override
	public QuizRatingStats ratings(int id) throws ServiceException {
		
		QuizRatingStats stats = getFromCache(id, this.mCacheStats);
		
		if (stats != null) {
			return stats;
		}
		
		return cache(id, this.mRatingsService.ratings(id), this.mCacheStats);
	}

	@Override
	public QuizRating rating(int id) throws ServiceException {
		
		QuizRating rating = getFromCache(id, this.mCacheRating);
		
		if (rating != null) {
			return rating;
		}
		
		return cache(id, this.mRatingsService.rating(id), this.mCacheRating);
	}

	@Override
	public void rate(int id, QuizRating rating) throws ServiceException {
		if (isOffline()) {
			this.mQueueRating.add(new RatingWrapper(id, rating));
		} else {
			this.mRatingsService.rate(id, rating);
		}
	}

	@Override
	public void flush() throws ServiceException {
		while (!this.mQueueRating.isEmpty()) {
			RatingWrapper wrapper = this.mQueueRating.poll();
			this.mRatingsService.rate(wrapper.getId(), wrapper.getRating());
		}
	}

}
