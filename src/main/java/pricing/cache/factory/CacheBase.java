package pricing.cache.factory;



public abstract class CacheBase implements pricing.cache.factory.CacheInterface {
	protected String cacheName = null;

	/**
	 * <P>
	 * Public method used to create a primed instance of a cache
	 * 
	 * <P>
	 * During the initiation of the specific client caches the
	 * {@link com.siebel.CacheFactory.cache.factory.CfgCacheFactory} class ends
	 * up leveraging the Class.forName() method to create an instance of the
	 * cache and will call the cache createInstance() method create a primed
	 * instance of the cache that will be used during runtime.
	 * 
	 * @param name
	 *            which specified
	 * @return the a new fully primed instance of the cache class.
	 * 
	 */
	abstract public CacheInterface createInstance(String cacheName);
	
	/**
	 * <P>
	 * Utility Function to handle any cleanup when the cache is been released
	 * <P>
	 * The CfgCacheBase.release() is an no-op method that does nothing.
	 * 
	 */
	@Override
	public void release() { }
	
	/**
	 * <P>
	 * Utility Function to trace cache get() calls
	 * 
	 * @param key
	 * @param value
	 */
	public void trace_get(String key, String value) {
		// Trace if needed
	}

	/**
	 * <P>
	 * Utility Function to trace cache put() calls
	 * 
	 * @param key
	 * @param value
	 */
	public void trace_put(String key, String value) {
		// Trace if needed
	}
}
