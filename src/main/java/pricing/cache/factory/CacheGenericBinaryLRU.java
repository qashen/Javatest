package pricing.cache.factory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;


/**
 * <P>
 * CacheBinaryLRU is a simple LRU implementation that tries to limit the size
 * of the cache memory based on the byte limit. The class leverage the base LRU
 * functionality offered by the {@link java.util.LinkedHashMap} class and uses
 * the sizing estimates provided
 * {@link com.siebel.configurator.common.CfgObjectSizer} to try and control the
 * cache size to the requested limit.
 * 
 * @see CacheGenericInterface
 * @see java.util.LinkedHashMap
 * @version %I%, %G%
 * @since 1.7
 */
public class CacheGenericBinaryLRU<K, V> implements CacheGenericInterface<K, V> {
	private boolean accessOrder = true; // the ordering mode - true for
										// access-order, false for
										// insertion-order
	protected long size_limit = 50000; // the byte limit on the number of
										// elements
	protected float loadFactor = 0.75f; // the load factor
	protected int initialCapacity = 0; // the initial capacity

	protected CfgCacheGenericLRUHashMap cacheMap = null;
	private long cacheMapByteSize = 0;

	/**
	 * <P>
	 * CfgCacheGenericLRUHashMap is a simple class that wraps around a
	 * {@link java.util.LinkedHashMap} instance so that it's Access order mode
	 * is set to 'access-order' and not the default 'insertion-order'
	 *
	 * @author Martin Finnerty
	 * @see java.util.LinkedHashMap
	 * @version %I%, %G%
	 * @since 1.7
	 */
	protected class CfgCacheGenericLRUHashMap extends LinkedHashMap<K, V> {
		private static final long serialVersionUID = -1416386627070582669L;

		public CfgCacheGenericLRUHashMap() {
			super(CacheGenericBinaryLRU.this.initialCapacity, CacheGenericBinaryLRU.this.loadFactor,
					CacheGenericBinaryLRU.this.accessOrder);
		}

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
			return false;
		}
	}

	/**
	 * <P>
	 * Public constructor for the CfgCacheGenericBinaryLRU class. The parameter
	 * <tt>limitInBytes</tt> tells the size (in bytes) limit that is been placed
	 * on the cache.
	 *
	 * @param limitInBytes
	 *            Specify the in-memory limit for the cache
	 * @return Returns the value associated to the key.
	 */
	public CacheGenericBinaryLRU(long limitInBytes) {
		super();
		this.size_limit = limitInBytes;
		this.cacheMap = this.new CfgCacheGenericLRUHashMap();

		// see if the default size needs to be changed
		String value = Utilities.getResourceProperties()
				.getProperty(Constants.CACHE_INSTANCE_INMEMORY_BINARY_SIZE);

		if (value != null) {
			Integer newSize = Integer.parseInt(value);
			if (newSize != null && 0 < newSize)
				this.size_limit = newSize;
		}
	}

	/**
	 * <P>
	 * Utility method that returns the the current size (in bytes) of the objects stored in the cache.
	 *
	 * @return Returns the byte size of the objects been tracked in the cache.
	 */
	public long getByteSize() {
		return cacheMapByteSize;
	}

	/**
	 * <P>
	 * Call the base {@link java.util.LinkedHashMap} get() method to return the
	 * value associated to the <tt>key</tt> value.
	 *
	 * @param key
	 *            Specify the key been looked up
	 * @return Returns the value associated to the key.
	 */
	public V get(Object key) {
		return cacheMap.get(key);
	}

	/**
	 * <P>
	 * put(K key, V value) will place the key/value association into the
	 * internal cache map. Before it's does so it calculates the size of the Key
	 * and Value objects and if it detects that there is not enough space in
	 * cache the code will go into a while loop removing the Least Recently Used
	 * (LRU) Key/Value entry until there is enough space to store the new entry.
	 *
	 * @param key
	 *            Specify the key been set
	 * @param value
	 *            Specify the object that will be associated with the key
	 * @return Returns the value previously associated to the key.
	 */
	public V put(K key, V value) {
		long keyByteSize = 0;
		long valByteSize = 0;
		long oldValByteSize = 0;
		long reqMemByteSize = keyByteSize + valByteSize;

		// Get return value;
		V rtnVal = get(key);

		// If there an existing entry for this key then adjust the size
		reqMemByteSize -= oldValByteSize;

		synchronized (cacheMap) {

			// See if we hit the cache size limit
			if (this.size_limit < this.cacheMapByteSize + reqMemByteSize) {

				// Loop until we have enough space in the cache
				while (this.size_limit < (this.cacheMapByteSize + reqMemByteSize)) {
					// Since we looping to remove elements we need to get the
					// iterator every time otherwise an exception is thrown.
					Iterator<Entry<K, V>> iter = this.cacheMap.entrySet().iterator();
					if (iter.hasNext()) {
						Entry<K, V> next = iter.next();

						K lastKey = next.getKey();
						V lastVal = next.getValue();

						long entrySize = 0;

						this.cacheMap.remove(lastKey);
						this.cacheMapByteSize -= entrySize;
					}

					else {
						// No more elements so break out of while loop
						// If Level.WARNING is then log an WARNING log message
						if (Utilities.IsLogLevelLoggable(Level.WARNING)) {
							Utilities.logCfgMsg(Level.WARNING,
									String.format(
											"Exception: required cache size -- CfgCacheGenericBinaryLRU.put(%s,%s)[cacheMapSize=%d][requiredCacheSize=%d]",
											key.toString(), value.toString(), this.cacheMapByteSize, reqMemByteSize));
						}
						break;
					}
				}
			}

			cacheMap.put(key, value);
			this.cacheMapByteSize += reqMemByteSize;
		}
		return rtnVal;
	}

	/**
	 * <P>
	 * Remove the key/value from the cache and adjust the memory cache size.
	 *
	 * @param key
	 *            Specify the key been set
	 * @return Returns the value previously associated to the key.
	 */
	public V remove(Object key) {
		// Get return value;
		V rtnVal = get(key);

		synchronized (cacheMap) {
			V value = cacheMap.remove(key);
			long keySize = 0 ;
			long valSize = 0;
			long objectSize = keySize + valSize;
			this.cacheMapByteSize -= objectSize;
		}

		return rtnVal;
	}

	/**
	 * <P>
	 * Return the number of elements in the cache.
	 *
	 * @return Returns the number of Key/Value entries in the cache.
	 */
	public int size() {
		synchronized (cacheMap) {
			return cacheMap.size();
		}
	}

	/**
	 * <P>
	 * Remove all the Key/Value elements in the cache.
	 */
	public void clearAll() {
		synchronized (cacheMap) {
			cacheMap.clear();
			this.cacheMapByteSize = 0;
		}
	}

	/**
	 * <P>
	 * Check the cache to see if there is an entry in the cache with the
	 * specified kay value.
	 *
	 * @return Returns <tt>true</tt> if there is an entry in the cache
	 *         associated to the specified key value, otherwise <tt>false</tt>
	 *         is returned.
	 */
	public boolean contains(K key) {
		synchronized (cacheMap) {
			return cacheMap.containsKey(key);
		}
	}
}
