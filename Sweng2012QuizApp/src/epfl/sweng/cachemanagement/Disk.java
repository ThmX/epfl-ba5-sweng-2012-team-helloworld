package epfl.sweng.cachemanagement;

import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

/**
 * 
 * @author thmx
 *
 */
public final class Disk implements IDisk {
	
	private static Disk disk;
	
	public static synchronized Disk getInstance() {
		if (disk == null) {
			disk = new Disk();
		}
		return disk;
	}
	
	private static final int DEFAULT_SIZE = 10;
	
		
	private ICache<Integer, JSONObject> mCache;
	
	private Disk() {
		this.mCache = new LruCache<Integer, JSONObject>(DEFAULT_SIZE);
	}

	@Override
	public synchronized void changeCacheStrategy(ICache<Integer, JSONObject> newCache) {
		if (newCache == null) {
			return;
		}
		
		if (this.mCache != null) {
			for (Entry<Integer, JSONObject> e : this.mCache.entrySet()) {
				newCache.put(e.getKey(), e.getValue());
			}
			this.mCache.clear();
		}
		this.mCache = newCache;
	}

	@Override
	public synchronized JSONObject get(Integer id) {
		return this.mCache.get(id);
	}

	@Override
	public synchronized void put(Integer id, JSONObject jObj) {
		this.mCache.put(id, jObj);
	}

	@Override
	public synchronized Set<Entry<Integer, JSONObject>> entrySet() {
		return this.mCache.entrySet();
	}

	@Override
	public synchronized void clear() {
		this.mCache.clear();
	}

	@Override
	public synchronized int size() {
		return this.mCache.size();
	}

}
