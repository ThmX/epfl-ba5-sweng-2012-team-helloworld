package epfl.sweng.services.proxy;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;

import android.util.SparseArray;
import epfl.sweng.cachemanagement.Disk;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.services.QuestionsService;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;

/**
 * Proxy to fetch question on the server.
 * 
 * @version 6.0.0 - 12.12.2012
 */
public class QuestionsProxyService extends AbstractProxyService implements QuestionsService {
	
	private QuestionsService mQuestionsService;
	
	private SparseArray<QuizQuestion> mCache = new SparseArray<QuizQuestion>();
	
	private Queue<QuizQuestion> mQueue = new LinkedBlockingQueue<QuizQuestion>(); 

	public QuestionsProxyService(SessionManager sessionManager, QuestionsService questionsService) {
		super(sessionManager);
		this.mQuestionsService = questionsService;
	}

	@Override
	public QuizQuestion random() throws ServiceException {
		if (isOffline()) {
			if (this.mCache.size() == 0) {
				throw new ServiceException(new ProxyServiceException("There is no question in cache."));
			}
			
			Random rand = new Random();
			int size = this.mCache.size();
			int key = this.mCache.keyAt(rand.nextInt(size));
			
			return this.mCache.get(key);
		}
		
		QuizQuestion question = this.mQuestionsService.random();
		try {
			Disk.getInstance().put(question.getID(), question.toJSON());
		} catch (JSONException e) {
			throw new ServiceException(e);
		}
		this.mCache.put(question.getID(), question);
		return question; 
	}

	@Override
	public QuizQuestion get(int id) throws ServiceException {
		
		QuizQuestion question = getFromCache(id, this.mCache);
		
		if (question != null) {
			return question;			
		}
		
		return cache(id, this.mQuestionsService.get(id), this.mCache);
	}

	@Override
	public void delete(int id) throws ServiceException {
		if (isOffline()) {
			throw new ServiceException(new ProxyServiceException("Can't execute command while offline."));
		}
		
		this.mQuestionsService.delete(id);
	}

	@Override
	public QuizQuestion post(QuizQuestion newQuestion) throws ServiceException {
		if (isOffline()) {
			this.mQueue.add(newQuestion);
			return newQuestion;
		}
		
		QuizQuestion question = this.mQuestionsService.post(newQuestion);
		try {
			Disk.getInstance().put(question.getID(), question.toJSON());
		} catch (JSONException e) {
			throw new ServiceException(e);
		}
		return question;
	}

	@Override
	public void flush() throws ServiceException {
		while (!this.mQueue.isEmpty()) {
			
			QuizQuestion question = this.mQuestionsService.post(this.mQueue.poll());
			try {
				Disk.getInstance().put(question.getID(), question.toJSON());
			} catch (JSONException e) {
				throw new ServiceException(e);
			}
		}
	}

}
