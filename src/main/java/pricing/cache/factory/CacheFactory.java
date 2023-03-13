package pricing.cache.factory;

import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;


/**
 * <P>
 * The CacheFactory implements the {@link CacheFactoryInterface} interface
 * and create the cache instances that will be used within the 
 * 
 */
public class CacheFactory implements CacheFactoryInterface {
	/**
	 * The HashMap<String, CfgCacheInterface> member variable used to store the
	 * cache instances.
	 */
	protected final HashMap<String, CacheInterface> mapCaches = new HashMap<String, CacheInterface>();

	/**
	 * getCacheNames() is a utility method that returns a names of the caches
	 * currently known to the cache factory
	 * 
	 * @return A Set<String> containing the names of all the caches.
	 */
	public Set<String> getCacheNames() {
		return mapCaches.keySet();
	}

	/**
	 * getMapCaches() is a utility method that returns the Map<String,
	 * CfgCacheInterface> instance
	 * 
	 * @return A Set<String> containing the names of all the caches.
	 */
	public Map<String, CacheInterface> getMapCaches() {
		return mapCaches;
	}

	/**
	 * Initialization method that is called to create the caches needed for
	 * Configurator.
	 *
	 * @param metadata_cls
	 *            metadata_cls specifies the class to be used to store the
	 *            Configurator metadata
	 * @param instance_cls
	 *            instance_cls specifies the class to be used to store the ACT
	 *            Model default instances
	 * @param message_cls
	 *            message_cls specifies the class to be used to communicate the
	 *            runtime requests and responses
	 * @throws InvalidClassException
	 *             if a java class can't be access by the factory
	 * @throws InvalidObjectException
	 *             if an instance of the cache can't be created by the factory
	 */
	public void init(String metadata_cls)
			throws InvalidObjectException, InvalidClassException {
		Object instance = null;

		synchronized (mapCaches) {
			instance = Utilities.createInstance(metadata_cls);
			if (instance instanceof CacheBase) {
				instance = ((CacheBase) instance).createInstance(CacheFactoryType.METADATA_CACHE.toString());
				setCache(CacheFactoryType.METADATA_CACHE, (CacheBase) instance);
			} else {
				Utilities.logCfgMsg(Level.SEVERE, "Invalid CfgCacheInstance class: " + metadata_cls);
				throw new InvalidClassException(metadata_cls, "Class in not extended from CfgCacheBaseInstance");
			}

		}

		// If Level.FINE is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINE)) {
			Utilities.logCfgMsg(Level.FINE,
					String.format("CfgCacheFactory.init()[matadata class: %s][message class: %s][instance class: %s]",
							metadata_cls));
		}
	}

	/**
	 * Returns a reference to the cache instance that is been requested.
	 *
	 * @param cache
	 *            cache specifies the cache name been requested
	 * @throws InvalidObjectException
	 *             if an instance of the cache is not accessible by the factory
	 */
	@Override
	public CacheInterface getCache(CacheFactoryType cache) throws InvalidObjectException {
		return getCache(cache.toString());
	}

	/**
	 * getCache(String) is a utility method that returns a cache associated to
	 * string that is passed in
	 * 
	 * @param cache
	 *            cache specifies the cache name that you want
	 * 
	 * @return the cache associated to the cache string. If no cache is found
	 *         then <code>null</code> is returned.
	 */
	public CacheInterface getCache(String cache) throws InvalidObjectException {
		CacheInterface ret_val = null;
		synchronized (mapCaches) {

			// Check and see if there is something associated to that key
			if (mapCaches.containsKey(cache)) {
				ret_val = mapCaches.get(cache);
			} else {
				// Generate a detail error message
				Utilities.logCfgMsg(Level.SEVERE, String.format("CfgCacheFactory.getCache(%s) failed", cache));

				throw new InvalidObjectException("Invalid Cache name: " + cache);
			}
		}
		return ret_val;
	}

	/**
	 * Register an associate of a cache with a specific CfgCacheInterface
	 * instance object.
	 *
	 * @param cache
	 *            cache specifies the cache name
	 * @param cacheInstance
	 *            cacheInstance specifies the cache instance that will be mapped
	 *            to the cache
	 */
	public void setCache(CacheFactoryType cache, CacheInterface cacheInstance) {
		this.setCache(cache.toString(), cacheInstance);
	}

	/**
	 * Register an associate of a cache with a specific CfgCacheInterface
	 * instance object.
	 *
	 * @param cache
	 *            cache specifies the cache name
	 * @param cacheInstance
	 *            cacheInstance specifies the cache instance that will be mapped
	 *            to the cache
	 */
	public void setCache(String cache, CacheInterface cacheInstance) {
		synchronized (mapCaches) {
			// Check and see if the cache map has already that key
			if (mapCaches.containsKey(cache)) {
				mapCaches.remove(cache);

				// If Level.WARNING is set then log an info log message
				if (Utilities.IsLogLevelLoggable(Level.WARNING)) {
					// Generate a log message
					Utilities.logCfgMsg(Level.WARNING,
							String.format("CfgCacheFactory.setCache(%s, %s) replacing existing instance", cache,
									cacheInstance.getClass().getName()));
				}
			}

			// Update the cache map with key/value that was passed in
			mapCaches.put(cache, cacheInstance);
		}
	}
}
