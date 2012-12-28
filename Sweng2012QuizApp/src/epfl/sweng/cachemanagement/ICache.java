package epfl.sweng.cachemanagement;

import java.util.Map;
import java.util.Set;

/**
 * Interface for a cache strategy that maps keys to values and can be queried by key
 *  @param <K> - the type of the keys
 *  @param <V> - the type of the values in the cache
 */
public interface ICache<K, V> {

    /**
     * Caches @param value of @param key
     * Counts as an access.
     * @param key
     * @param value
     * @return the value or {@code null} if the key is {@code null}
     */
    
    V put(K key, V value);
    
    /**
     * Get the cached value for key
     * @param key the key
     * @return the value for key or {@code null} if the key does not exist or is {@code null}
     */
    V get(K key);
    
    /**
     * @return the cache size
     */
    int size();
    
    /**
     * Get all entries in the cache
     * Does not count as an access.
     * @return a {@code Set} of {@code Map.Entry} representing all the entries in the cache
     */
    Set<Map.Entry<K, V>> entrySet();
    
    /**
     * Clear (empty) the cache
     */
    void clear();
}
