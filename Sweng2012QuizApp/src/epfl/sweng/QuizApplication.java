package epfl.sweng;

import org.apache.http.HttpMessage;

import epfl.sweng.services.AuthenticationException;
import epfl.sweng.services.QuestionsService;
import epfl.sweng.services.QuizService;
import epfl.sweng.services.RatingsService;
import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;
import epfl.sweng.services.TequilaException;
import epfl.sweng.services.TequilaService;
import epfl.sweng.services.impl.DefaultQuestionsService;
import epfl.sweng.services.impl.DefaultRatingsService;
import epfl.sweng.services.impl.DefaultTequilaService;
import epfl.sweng.services.proxy.QuestionsProxyService;
import epfl.sweng.services.proxy.RatingsProxyService;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Base Application
 * 
 * @version 1.0.0 - 2012.09.29 - Initial version
 * @version 3.0.0 - 2012.10.31 - Add TequilaService
 * @version 5.0.0 - 2012.11.28 - Add QuizService
 * @version 6.0.0 - 2012.12.12 - Add Offline + OfflineService
 */
public class QuizApplication extends Application implements SessionManager {
	
	private static final String USER_SESSION = "user_session";
	private static final String SESSION_ID = "SESSION_ID";

	private QuestionsProxyService mQuestionsService;
	private RatingsProxyService mRatingsService;
	private TequilaService mTequilaService;
	private QuizService mQuizService;
	
	private boolean mOffline;

	/**
	 * Lazy instantiation of a QuestionsService
	 */
	public QuestionsService getQuestionsService() {
		if (this.mQuestionsService == null) {
			String urlBase = getString(R.string.quizquestions_url);
			this.mQuestionsService = new QuestionsProxyService(this, new DefaultQuestionsService(urlBase, this));
		}
		return this.mQuestionsService;
	}
	
	/**
	 * Lazy instantiation of a QuestionsService
	 */
	public RatingsService getRatingsService() {
		if (this.mRatingsService == null) {
			String urlBase = getString(R.string.quizquestions_url);
			this.mRatingsService = new RatingsProxyService(this, new DefaultRatingsService(urlBase, this));
		}
		return this.mRatingsService;
	}
	
	/**
	 * Lazy instantiation of a TequilaService
	 */
	public TequilaService getTequilaService() {
		if (this.mTequilaService == null) {
			String urlLogin = getString(R.string.quizlogin_url);
			String urlTequila = getString(R.string.tequila_url);
			this.mTequilaService = new DefaultTequilaService(urlLogin, urlTequila);
		}
		return this.mTequilaService;
	}
	
	/**
	 * Lazy instantiation of a QuizService
	 */
	public QuizService getQuizService() {
		if (this.mQuizService == null) {
			String urlBase = getString(R.string.quizzes_url);
			this.mQuizService = new QuizService(urlBase, this);
		}
		return this.mQuizService;
	}
	
	
	public void authenticate(String username, String password) throws TequilaException {

		clearSession();
		
		// Step 1 to 6
		String session = getTequilaService().authenticate(username, password);

		// Step 7
		SharedPreferences userSession = getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE);
		Editor editor = userSession.edit();
		editor.putString(SESSION_ID, session);
		editor.commit();
	}
	
	@Override
	public boolean isAuthenticated() {
		SharedPreferences userSession = getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE);
		return userSession.contains(SESSION_ID);
	}
	
	@Override
	public void clearSession() {
		this.mOffline = false;

		SharedPreferences userSession = getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE);
		Editor editor = userSession.edit();
		editor.remove(SESSION_ID);
		editor.commit();
		
	}
	
	@Override
	public String getSession() throws AuthenticationException {
		if (!isAuthenticated()) {
			throw new AuthenticationException("Unauthenticated");
		}
		
		SharedPreferences userSession = getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE);
		return userSession.getString(SESSION_ID, null);
	}

	@Override
	public void addAuthentication(HttpMessage httpMessage) throws AuthenticationException {
		httpMessage.setHeader("Authorization", "Tequila " + getSession());
	}
	
	@Override
	public void setOffline(boolean offline) throws ServiceException {
		if (!offline) {
			((QuestionsProxyService) getQuestionsService()).flush();
			((RatingsProxyService) getRatingsService()).flush();
		}
		
		this.mOffline = offline;
	}

	@Override
	public boolean isOffline() {
		return this.mOffline;
	}
}
