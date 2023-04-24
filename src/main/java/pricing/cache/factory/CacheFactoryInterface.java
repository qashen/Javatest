package pricing.cache.factory;

import java.util.Map;


/**
* <P> The CacheFactoryInterface interface is the root interface that is used to 
* define the Cache Factory API 
*    
*      
*/
public interface CacheFactoryInterface {
	public Map<String, CacheInterface> getMapCaches();	
	
	/**
     * <P>Queries the factory object to obtain a reference to a specific cache instance   
     * 
     * @param cache cache provides the details of the cache that is been requested 
     * @return A reference to the cache 
     * @throws InvalidObjectException if cache can't be accessed by the factory
     */	
	CacheInterface getCache(CacheFactoryType cache) throws java.io.InvalidObjectException;
	CacheInterface getCache(String cache) throws java.io.InvalidObjectException; 
}
