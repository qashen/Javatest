package pricing.cache.factory;

import java.io.File;

/**
 * <P>
 * The Constants interface is the wrapper object containing all the constants
 * 
 * @version %I%, %G%
 * @since 1.7
 */
public interface Constants {
	final static String CACHE_TYPE="cache.type";
	final static String CACHE_INSTANCE_INMEMORY_BINARY_SIZE="";
	final static String SIEBEL_AUTH_TOKEN_KEY = "siebel.authorization.token";
	final static String PROMO_METADATA_CACHE = "com-siebel-persist-com-promo-metadata";
	final static String DEFAULT_CACHE_FACTORY_CLASS = "com.siebel.configurator.cache.factory.CfgCacheFactory";
	final static String DEFAULT_CACHE_CLASS = "com.siebel.configurator.cache.factory.CfgSiebelCoherenceClient";
	final static String CACHE_FACTORY_CLASS_NAME_KEY = "configurator.cache.class.name.factory";

}
