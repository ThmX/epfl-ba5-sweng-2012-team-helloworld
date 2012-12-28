package epfl.sweng.test.exam4.student;

import org.json.JSONObject;

import android.test.InstrumentationTestCase;
import epfl.sweng.cachemanagement.Disk;
import epfl.sweng.cachemanagement.LruCache;
import epfl.sweng.cachemanagement.MfuCache;

/**
 * 
 * @author thmx
 *
 */
public class CacheManagementTests extends InstrumentationTestCase {

	public void testMFUWrongCacheSize() {
		try {
			new MfuCache<Object, Object>(-1);
			fail("Expecting IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Nothing to do
		}
	}
	
	public void testLRUWrongCacheSize() {
		try {
			new LruCache<Object, Object>(-1);
			fail("Expecting IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Nothing to do
		}
	}
	
	public void testMFUEvictAndClear() {
		final int maxSize = 3;
		MfuCache<Object, Object> cache = new MfuCache<Object, Object>(maxSize);
		
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		Object k4 = new Object();
		Object k5 = new Object();
		Object k6 = new Object();
		
		cache.put(k1, new Object());
		cache.get(k1);

		cache.put(k2, new Object());
		cache.get(k2);
		cache.get(k2);
		
		cache.put(k3, new Object());
		cache.get(k3);
		
		cache.put(k4, new Object());
		cache.get(k4);
		cache.get(k4);
		
		cache.put(k5, new Object());
		cache.get(k5);
		cache.get(k5);
		cache.get(k5);
		
		cache.put(k6, new Object());
		
		assertEquals(maxSize, cache.size());
		
		cache.clear();
		
		assertEquals(0, cache.size());
	}
	
	public void testDisk() {
		final int maxSize = 5;

		Disk disk = Disk.getInstance();
		
		disk.changeCacheStrategy(null);
		
		disk.changeCacheStrategy(new MfuCache<Integer, JSONObject>(maxSize));
		
		disk.put(1, new JSONObject());
		disk.put(2, new JSONObject());
		
		disk.changeCacheStrategy(new LruCache<Integer, JSONObject>(maxSize));
		
		assertNotNull(disk.get(1));
		
		disk.entrySet();
		
		assertEquals(2, disk.size());
		
		disk.clear();
		
		assertEquals(0, disk.size());
	}
}
