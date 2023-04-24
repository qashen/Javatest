package pricing.cache.factory;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;


/**
 * <P>
 * CacheSimple is the simple implementation of the {@link CacheBase} class
 * using a HashMap<String, String> to store the key/value pairing.
 * @see HashMap
 * @see Set
 * @version %I%, %G%
 * @since 1.7
 */
public class CacheSimple extends CacheBase {

	/**
	 * The HashMap<String, String> member variable used to store the cache
	 * Key/Value settings.
	 */
	protected HashMap<String, String> cacheMap = null;

	/**
	 * The name of the cache.
	 */
	protected String cache_name = null;

	public CacheSimple() {
		super();
	}

	public CacheSimple(String cache_name) {
		this();
		this.cache_name = cache_name;
		this.cacheName=cache_name;
		cacheMap = new HashMap<String, String>();

		// If Level.FINER is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINER)) {
			Utilities.logCfgMsg(Level.FINER, String.format("CfgCacheSimple(%s)", cache_name));
		}
	}

	/**
	 * Creates a fully primed CfgCacheSimple instance.
	 *
	 * @param cacheName
	 *            name that the new instance will associate itself with
	 * @return a reference to a new fully primed CfgCacheSimple instance.
	 */
	@Override
	public CacheInterface createInstance(String cacheName) {
		CacheInterface ret_val = new CacheSimple(cacheName);

		// If Level.FINE is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINE)) {
			Utilities.logCfgMsg(Level.FINE, String.format("CfgCacheSimple.createInstance(%s)", cache_name));
		}
		return ret_val;
	}

	/**
	 * Removes all of the Key/Value pairing that is stored in the cache.
	 */
	@Override
	public void clearAll() {
		// No simple removeAll API so need to loop through each key
		synchronized (cacheMap) {
			cacheMap.clear();
		}

		// If Level.FINE is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINE)) {
			Utilities.logCfgMsg(Level.FINE, String.format("[%s]CfgCacheSimple::clearAll()", this.cache_name));
		}
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings
	 * otherwise <tt>false</tt>.
	 *
	 * @param key
	 *            Cache key to see if there is any entry in the cache
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 *         otherwise <tt>false</tt>.
	 */
	@Override
	public boolean contains(String key) {
		boolean bVal = false;
		synchronized (cacheMap) {
			bVal = cacheMap.containsKey(key);
		}
		return bVal;
	}

	/**
	 * Returns the value associated to the key in the cache, or {@code null} if
	 * this cache does not contains any value associated with the key.
	 *
	 * @param key
	 *            key with which the specified key value been looked up
	 * @return the value associated with <tt>key</tt>, or <tt>null</tt> if there
	 *         was no mapping for <tt>key</tt>.
	 */
	@Override
	public String getNoTrace(String key) {
		String prevVal = null;
		synchronized (cacheMap) {
			prevVal = cacheMap.get(key);
		}

		return prevVal;
	}

	/**
	 * Returns the value associated to the key in the cache, or {@code null} if
	 * this cache does not contains any value associated with the key.
	 *
	 * @param key
	 *            key with which the specified key value been looked up
	 * @return the value associated with <tt>key</tt>, or <tt>null</tt> if there
	 *         was no mapping for <tt>key</tt>.
	 */
	@Override
	public String get(String key) {
		String prevVal = getNoTrace(key);

		super.trace_get(key, prevVal);
		return prevVal;
	}

	/**
	 * Associates the specified value with the specified key in this cache. If
	 * the cache already contains a mapping for the key, then this value is
	 * replaced and returned.
	 *
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
	 *         if there was no mapping for <tt>key</tt>.
	 */
	@Override
	public String put(String key, String value) {
		String prevVal = null;
		synchronized (cacheMap) {
			prevVal = cacheMap.put(key, value);
		}

		super.trace_put(key, value);
		return prevVal;
	}

	/**
	 * Remove the Key/Value pairing associated with the specified key from the
	 * cache. If the cache previously contained a mapping for the key that value
	 * is returned.
	 *
	 * @param key
	 *            key specifies the association that will be removed from the
	 *            cache
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
	 *         if there was no mapping for <tt>key</tt>.
	 */
	@Override
	public String remove(String key) {
		String prevVal = null;
		synchronized (cacheMap) {
			prevVal = cacheMap.remove(key);
		}

		// If Level.FINE is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINE)) {
			Utilities.logCfgMsg(Level.FINE, 
					String.format("[%s]CfgCacheSimple::remove(%s)", this.cache_name, key));
		}

		return prevVal;
	}

	/**
	 * Returns the number of key-value mappings in this cache.
	 *
	 * @return the number of key-value mappings in this cache
	 */
	@Override
	public int size() {
		synchronized (cacheMap) {
			return cacheMap.size();
		}
	}

	/**
	 * Returns the set view of keys in this cache.
	 *
	 * @return the set view of keys in this cache.
	 */
	@Override
	public Set<String> keySet() {
		Set<String> rtnSet = null;

		if (null != cacheMap) {
			rtnSet = cacheMap.keySet();
		}

		return rtnSet;
	}
}
