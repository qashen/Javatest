package pricing.cache.factory;

/**
* <P> The CacheGenericInterface<K, V> interface is the root interface.
*  This allows a loose coupling between the client and the actual class 
*   that implements a specific cache implementation.     
*      
* @version     %I%, %G%
* @since   1.7
*/
public interface CacheGenericInterface<K, V> {
	/**
     * Removes all of the Key/Value pairing that is stored in the cache and the cache will be empty after this call returns.
     */
	void clearAll();
	
	/**
     * Returns <tt>true</tt> if cache contains a Key value associated to the key otherwise it will return <tt>false</tt>  
     * 
     * @param key key is value to be tested to see if there is a <Key/Value> association in the cache for this key
     * @return <tt>true</tt> if this map contains a mapping for the specified key otherwise <tt>false</tt> is returned
     */	
	boolean contains(K key);
	
    /**
     * Returns the value associated to the specified key in the cache, 
     *   or {@code null} if there is no value associated to the key.
     *
     * @param key The key whose associated value is to be returned
     * @return the value associated to the specified key, or {@code null} 
     *    if this cache has no association to the passed in key value
     */
	V get(K key);
	
	/**
     * Associates the specified value with the specified  key in the cache.  
     * If the cache has a mapping for the key, the old value is replaced by the specified value. 
     *
     * @param key key with which the specified value is to be associated in the cache
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for specific <tt>key</tt>.
     */
    V put(K key,  V value);

    /**
     * <P>Removes the mapping for a key in the cache if it is present and will returns the value to  
     *   which the key was previously associated the key, or <tt>null</tt> if the cache contained no mapping for the specific key.
     *
     * <p>The cache will not contain a mapping for the specified key once the call returns.
     *
     * @param key key whose mapping is to be removed from the cache
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping found for the <tt>key</tt>.
     */
	V remove(K key);

    /**
     * Returns the number of key-value mappings in this cache.  
     *
     * @return the number of key-value mappings in this map
     */
	int size();
}
