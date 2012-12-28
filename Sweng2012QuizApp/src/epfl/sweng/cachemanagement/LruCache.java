package epfl.sweng.cachemanagement;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU cache implementation.
 * @param <K> the key
 * @param <V> the value
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> implements ICache<K, V> {

    private final int mMaxSize;
    private final static float LOAD_FACTOR = 0.75f;
    private final static boolean ACCESS_ORDER = true;
    private static final long serialVersionUID = -1234582265865921787L;
    
    public LruCache(int maxSize) {
        super(maxSize/2 + 1, LOAD_FACTOR, ACCESS_ORDER);
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize should be > 0");
        }
        mMaxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > mMaxSize;
    }
    
}
