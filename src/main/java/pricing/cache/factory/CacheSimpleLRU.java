package pricing.cache.factory;

/**
 * <P>
 * <K, V> is a simple LRU implementation that limits the size
 *    of the cache entries. It extends CfgCacheGenericBinaryLRU and overwrite the 
 *    put(K key, V value) logic so that the number of entries is check and 
 *    not the object size
 * 
 * @see CacheGenericBinaryLRU
 * @version %I%, %G%
 * @since 1.7
 */
public class CacheSimpleLRU<K, V> extends CacheGenericBinaryLRU<K, V> {
	public CacheSimpleLRU(int sizeLimit)  {
		super(sizeLimit);
		this.cacheMap = this.new CfgCacheSimpleLRUHashMap();
	}

	/**
	 * <P>CfgCacheSimpleLRUHashMap is a simple class that wraps around a
	 * {@link java.util.LinkedHashMap} instance so that it's Access order mode
	 * is set to 'access-order' and not the default 'insertion-order' 
	 *
	 * @author Martin Finnerty
	 * @version %I%, %G%
	 * @since 1.7
	 */
	protected class CfgCacheSimpleLRUHashMap extends CfgCacheGenericLRUHashMap {
		private static final long serialVersionUID = -1416386627070582669L;
		
		public CfgCacheSimpleLRUHashMap() {
			super();
		}

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
			return this.size() > size_limit;
		}
	}
	
	/**
	 * <P>
	 * Overwrite the inherited put(K key, V value) so that the LRU eviction logic 
	 * is driven by the number of entries in the cache and not the memory that
	 * will be taken up by the Key/Value pair.
	 *
	 * @param key
	 *            Specify the key been set
	 * @param value
	 *            Specify the object that will be associated with the key
	 * @return Returns the value previously associated to the key.
	 */
	public V put(K key, V value) {
		V rtnVal = get(key);

		synchronized (this.cacheMap) {
			cacheMap.put(key, value);
		}
		
		return rtnVal;
	}
}
