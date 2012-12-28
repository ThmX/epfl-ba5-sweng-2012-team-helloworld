package epfl.sweng.cachemanagement;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

/**
 * Interface that represents a disk that can be configured with different caching strategies
 */

public interface IDisk {
    
    /**
     * change the cache strategy
     * @param newCache - the new cache strategy
     */
    void changeCacheStrategy(ICache<Integer, JSONObject> newCache);
    
    /**
     * Gets question with id = {@code id} and updates the access to this {@code id}
     * @param {@code id} - the id of the touched question
     * @return the {@code JSONObject} if it is in the cache and {@code null} otherwise
     */
    JSONObject get(Integer id);
    
    /**
     * Add a question to the cache
     * @param id - the id of the question 
     * @param jObj - the {@code JSONObject} representing this question
     */
    void put(Integer id, JSONObject jObj);
    
    /**
     * @return all the entries in the cache
     */
    Set<Map.Entry<Integer, JSONObject>> entrySet();
    
    /**
     * Empty the cache.
     */
    void clear();

    /**
     * @return the current size of the cache
     */
    int size();
}
