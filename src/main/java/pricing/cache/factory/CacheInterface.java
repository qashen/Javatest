package pricing.cache.factory;

import java.util.Set;

/**
* <P> The CacheInterface interface is the main cache interface that all the client code interacts
*   
*   <P>This interface extends the CfgCacheGenericInterface<K, V> interface and sets the 
*      generics <K, V> to be <String, String> since Strings are the only supported POF type in the 
*      Siebel coherence model with external systems.
*      
* @version	%I%, %G%
* @since   	1.7
*/
public interface CacheInterface extends CacheGenericInterface<String, String>
{
	void clearAll();
	boolean contains(String key);
	String get(String key);
	String getNoTrace(String key);
	String put(String key, String value);
	String remove(String key);
	int size();

	Set<String> keySet();
	void trace_get(String key, String value);
	void trace_put(String key, String value);
	void release();

}
