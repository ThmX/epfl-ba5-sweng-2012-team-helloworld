package epfl.sweng.services.proxy;

import android.util.SparseArray;

import epfl.sweng.services.ServiceException;
import epfl.sweng.services.SessionManager;

/**
 * 
 * @author thmx
 *
 */
public abstract class AbstractProxyService {

	private SessionManager mSessionManager;
	
	public AbstractProxyService(SessionManager sessionManager) {
		this.mSessionManager = sessionManager;
	}
	
	public SessionManager getSessionManager() {
		return this.mSessionManager;
	}
	
	public boolean isOffline() {
		return (this.mSessionManager != null) && this.mSessionManager.isOffline();
	}
	
	public <T> T getFromCache(int id, SparseArray<T> cache) throws ServiceException {
		T obj = null;
		
		if (isOffline()) {
			obj = cache.get(id);

			if (obj == null) {
				throw new ServiceException(new ProxyServiceException("Can't execute command while offline."));
			}
		}
		
		return obj;
	}
	
	public <T> T cache(int id, T obj, SparseArray<T> cache) {
		if (obj != null) {
			cache.put(id, obj);
		}
		
		return obj;
	}
	
	abstract public void flush() throws ServiceException;
	
}
