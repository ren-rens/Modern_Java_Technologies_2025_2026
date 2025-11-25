package bg.sofia.uni.fmi.mjt.pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheTest {

    @Test
    void testCacheValueIfKeyIsNull() {
        Cache cache = new Cache();

        assertThrows(IllegalArgumentException.class,
            () -> cache.cacheValue(null, new Object()));
    }

    @Test
    void testCacheValueIfValueIsNull() {
        Cache cache = new Cache();

        assertThrows(IllegalArgumentException.class, () -> cache.cacheValue(new Object(), null),
            "Testing CacheTest.cacheValue with value = null, should throw IllegalArgumentException");
    }

    @Test
    void testCacheValueValueAndKeyNull() {
        Cache cache = new Cache();

        assertThrows(IllegalArgumentException.class, () -> cache.cacheValue(null, null),
            "Testing CacheTest.cacheValue with value = null and key = null, should throw IllegalArgumentException");
    }

    @Test
    void testGetCachedValueKeyNull() {
        Cache cache = new Cache();

        assertThrows(IllegalArgumentException.class, () -> cache.getCachedValue(null),
            "Testing CacheTest.getCachedValue with key = null, should throw IllegalArgumentException");
    }

    @Test
    void testGetCachedValueWithKeyNotInCache() {
        Cache cache = new Cache();
        cache.clear(); // clear the cache to ensure returning false in contains

        // Test that getting a non-existent key returns null
        Object result = cache.getCachedValue(1);

        assertNull(result, "When getting a cached value with non-existed key should return null");
        //assertFalse(cache.containsKey(1), "When checking a hash for non-existed key should return false");
    }

    @Test
    void testGetCachedValueWithKeyInCache() {
        Cache cache = new Cache();
        cache.cacheValue(1, 2);

        Object result = cache.getCachedValue(1);

        assertEquals(2, result, "When checking a cache for existing key-value should return the value of the key");
    }

    @Test
    void testContainsKeyWithKeyNull() {
        Cache cache = new Cache();

        assertThrows(IllegalArgumentException.class, () -> cache.containsKey(null),
            "When key = null given to containsKey() should throw IllegalArgumentException");
    }

    @Test
    void testContainsKeyWithKeyNotInCache() {
        Cache cache = new Cache();
        cache.clear(); // to ensure the cache is empty

        assertFalse(cache.containsKey(1), "When key is not present in cache containsKey should return false");
    }

    @Test
    void testContainsKeyWithKeyInCache() {
        Cache cache = new Cache();
        cache.cacheValue(1, 2);

        assertTrue(cache.containsKey(1), "When key is present in cache containsKey should return true");
    }

    @Test
    void testIsEmptyTrue() {
        Cache cache = new Cache(); // when created it is empty

        assertTrue(cache.isEmpty(), "When cache is empty isEmpty should return true");
    }

    @Test
    void testIsEmptyFalse() {
        Cache cache = new Cache(); // when created it is empty
        cache.cacheValue(1, 2); // caching something so it is not empty

        assertFalse(cache.isEmpty(), "When cache is not empty isEmpty should return false");
    }

    @Test
    void testCacheOverwriteExistingKey() {
        Cache cache = new Cache();
        cache.cacheValue("key", "value1");
        cache.cacheValue("key", "value2");

        assertEquals("value2", cache.getCachedValue("key"));
    }

    @Test
    void testClearRemovesAllEntries() {
        Cache cache = new Cache();
        cache.cacheValue("key1", "value1");
        cache.cacheValue("key2", "value2");

        cache.clear();

        assertTrue(cache.isEmpty());
        assertNull(cache.getCachedValue("key1"));
        assertNull(cache.getCachedValue("key2"));
    }
}
