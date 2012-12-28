package epfl.sweng.test.exam4.student;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.test.InstrumentationTestCase;

import epfl.sweng.cachemanagement.ICache;
import epfl.sweng.cachemanagement.LruCache;
import epfl.sweng.cachemanagement.MfuCache;

/**
 * Cache unit tests, do not require a GUI to run.
 */
public class CacheUnitTests extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testLruCacheFunctionality() {
        ICache<Integer, Integer> cache = new LruCache<Integer, Integer>(3);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(1);
        cache.get(3);
        cache.put(4, 4);
        cache.get(4);

        Set<Integer> keys = new TreeSet<Integer>();

        for (Map.Entry<Integer, Integer> entry : cache.entrySet()) {
            keys.add(entry.getKey());
        }

        Set<Integer> expectedKeys = new TreeSet<Integer>();
        expectedKeys.add(1);
        expectedKeys.add(3);
        expectedKeys.add(4);

        assertTrue(
                "cache size = 3; after the following operations (on keys): "
                        + "put(1), put(2), put(3), get(1), get(2), get(3), get(1), get(3), put(4), get(4) "
                        + "key 2 is least used, cache should have keys "
                        + expectedKeys + " but has keys " + keys,
                keys.equals(expectedKeys));
    }

    public void testMfuCacheFunctionality() {
        ICache<Integer, Integer> cache = new MfuCache<Integer, Integer>(3);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(1);
        cache.get(1);
        cache.put(4, 4);

        Set<Integer> keys = new TreeSet<Integer>();

        for (Map.Entry<Integer, Integer> entry : cache.entrySet()) {
            keys.add(entry.getKey());
        }

        Set<Integer> expectedKeys = new TreeSet<Integer>();
        expectedKeys.add(2);
        expectedKeys.add(3);
        expectedKeys.add(4);

        assertTrue(
                "cache size = 3; after the following operations (on keys): "
                        + "put(1), put(2), put(3), get(1), get(2), get(3), get(1), get(1), get(1), put(4) "
                        + "key 1 is most frequently used, cache should have keys "
                        + expectedKeys + " but has keys " + keys,
                keys.equals(expectedKeys));
    }
}
