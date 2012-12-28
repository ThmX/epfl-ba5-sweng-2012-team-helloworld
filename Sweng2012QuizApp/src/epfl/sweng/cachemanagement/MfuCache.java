package epfl.sweng.cachemanagement;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author thmx
 *
 * @param <K>
 * @param <V>
 */
public class MfuCache<K, V> implements ICache<K, V> {
	
	private final int mMaxSize;
	
	private HashMap<K, V> mMap;
	private HashMap<K, Integer> mCounter;

	public MfuCache(int maxSize) {
		this.mMaxSize = maxSize;
		
		this.mMap = new HashMap<K, V>(maxSize);
		this.mCounter = new HashMap<K, Integer>(maxSize);
	}
	
	public void evictIfNeeded() {
		if (size() >= this.mMaxSize) {
			Entry<K, Integer> entry = null;
			for (Entry<K, Integer> e : this.mCounter.entrySet()) {
				if (entry == null) {
					entry = e;
				} else {
					if (entry.getValue() < e.getValue()) {
						entry = e;
					}
				}
			}
			
			if (entry != null) {
				K key = entry.getKey();
				this.mCounter.remove(key);
				this.mMap.remove(key);
			}
		}
	}
	
	@Override
	public V put(K key, V value) {
		evictIfNeeded();
		this.mCounter.put(key, 0);
		return this.mMap.put(key, value);
	}

	@Override
	public V get(K key) {
		this.mCounter.put(key, this.mCounter.get(key) + 1);
		return this.mMap.get(key);
	}

	@Override
	public int size() {
		return this.mMap.size();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.mMap.entrySet();
	}

	@Override
	public void clear() {
		this.mMap.clear();
	}

}
