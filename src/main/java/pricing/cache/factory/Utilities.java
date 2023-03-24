package pricing.cache.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map.Entry;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;



/**
 * <P>
 * The CfgUtilities class is the wrapper object containing generic utility
 * methods leveraged in the Siebel Configurator ACT integration codeline.
 * 
 * @author Martin Finnerty
 * @version %I%, %G%
 * @since 1.7
 */
public class Utilities {
	protected static String properties_file = null;
	protected static Properties properties = new Properties();
	protected static CacheFactory cacheFactory = null;
//	private static org.apache.logging.log4j.Logger LOG4J_CFG_LOGGER = null;



	static {
		// Prime the session context object
		//Properties props = new Properties();
		try {
		} catch (Exception e) {
		
		}
	}


	/**
	 * <P>
	 * Utility Function to create a instance of a specific class using the empty
	 * constructor
	 * 
	 * @param cls_name
	 *            cls_name is the full Class name of the object that will be
	 *            created
	 * @return A reference to a newly create object
	 * @throws InvalidClassException
	 *             if object could not be created
	 */
	public static Object createInstance(String cls_name) throws InvalidClassException {
		Object ret_val = null;
		Constructor<?> ctor = null;

		// try and obtain class empty constructor
		try {
			ctor = Class.forName(cls_name).getDeclaredConstructor();
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {

			// Generate a log message
			Utilities.logCfgMsg(Level.SEVERE, String.format("CfgUtilities.createInstance(%s) -- FAILED", cls_name));
			throw new InvalidClassException(cls_name, e.toString());
		}

		// Create a new Instance using the class empty constructor
		try {
			ret_val = ctor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// Generate a log message
			Utilities.logCfgMsg(Level.SEVERE,
					String.format("CfgUtilities.createInstance(%s) call to ctor.newInstance() FAILED", cls_name));

			throw new InvalidClassException(cls_name, e.toString());
		}

		// If Level.FINEST is set then log message
		if (Utilities.IsLogLevelLoggable(Level.FINEST)) {
			Utilities.logCfgMsg(Level.FINEST,
					String.format("CfgUtilities.createInstance(%s) -- exiting...", cls_name));
		}
		return ret_val;
	}

	/**
	 * <P>
	 * Utility Function to get an reference to the singleton Cache Factory
	 * instance
	 * 
	 * @return A CfgCacheFactoryInterface to the singleton Cache Factory
	 *         instance
	 * @throws ExceptionInInitializerError
	 *             if a instance of the cache factory can't be access
	 */
	public static CacheFactoryInterface getCacheFactory() {
		return Utilities.getCacheFactory(true);
	}

	/**
	 * <P>
	 * Utility Function to get an reference to the singleton Cache Factory
	 * instance
	 * 
	 * @param initialize
	 *            boolean value to tell us if we should initialize the Cache
	 *            Factory if it had not already been done.
	 * @return A CfgCacheFactoryInterface to the singleton Cache Factory
	 *         instance
	 * @throws ExceptionInInitializerError
	 *             if a instance of the cache factory can't be access
	 */
	public static CacheFactoryInterface getCacheFactory(boolean initialize) {

		// initialize the instance if the factory is not already created
		if (null == cacheFactory && initialize) {
			try {
				Utilities.initCacheFactory();
			} catch (InvalidClassException | InvalidObjectException e) {

				// Generate a log message
				Utilities.logCfgMsg(Level.SEVERE,
						"CfgUtilities.getCacheFactory() call to CfgUtilities.initCacheFactory() -- FAILED");

				throw new ExceptionInInitializerError(e);
			}
		}

		// If Level.FINEST is set then log an info log message
		if (Utilities.IsLogLevelLoggable(Level.FINEST)) {
			Utilities.logCfgMsg(Level.FINEST, "CfgCacheFactoryInterface getCacheFactory() -- exiting...");
		}
		return cacheFactory;
	}

	/**
	 * <P>
	 * Utility Function to return the property file contents loaded during
	 * initialization. The default property file name is
	 * CfgConstants.CONFIGURATOR_PROPERTIES_FILE_NAME
	 * 
	 * @return Returns the Properties object contains the values read from the
	 *         property file
	 * 
	 */
	public static Properties getResourceProperties() {
		return Utilities.properties;
	}

	/**
	 * <P>
	 * Utility Function to return the resource property file name. The default
	 * property file name is CfgConstants.CONFIGURATOR_PROPERTIES_FILE_NAME
	 * 
	 * @return Returns the resource property file name
	 * 
	 */
	public static String getResourcePropertiesFileName() {
		return Utilities.properties_file;
	}

	/**
	 * <P>
	 * Utility Function to return the free memory available
	 * 
	 * @return Returns the amount of free memory in MBs
	 */
	public static long getFreeMemoryMB() {
		return (Runtime.getRuntime().freeMemory()) / 1024 / 1024;
	}

	/**
	 * <P>
	 * Utility Function to initialize the configurator cache factory.
	 * <P>
	 * There are a number of caches that will be leveraged as part if the
	 * configurator solution and this method will create a class based on the
	 * resource property value associated to the
	 * CfgConstants.CACHE_FACTOR_CLASS_NAME_KEY to do the initialization.
	 * 
	 * <P>
	 * The Classes used to create the configurator caches will be the values
	 * retreived from the resource properties using the predefined values:
	 * CfgConstants.CACHE_METADATA_CLASS_NAME_KEY,
	 * CfgConstants.CACHE_INSTANCE_CLASS_NAME_KEY,
	 * CfgConstants.CACHE_MESSAGE_CLASS_NAME_KEY;
	 * 
	 * @throws An
	 *             InvalidClassException exception will be thrown if the caches
	 *             can't be initialized
	 */
	private static void initCacheFactory() throws InvalidClassException, InvalidObjectException {
		synchronized (Utilities.class) {

			// Obtain the class names that will be used
			String factory_cls = getResourceProperties().getProperty(Constants.CACHE_FACTORY_CLASS_NAME_KEY);

			if (factory_cls == null || factory_cls.isEmpty()) {
				factory_cls = Constants.DEFAULT_CACHE_FACTORY_CLASS;
			}

			// Create an instance of the cache factory
			Object instance = Utilities.createInstance(factory_cls);
			if (instance instanceof CacheFactory) {
				cacheFactory = (CacheFactory) instance;
			} else {
				// Generate a log message
				Utilities.logCfgMsg(Level.SEVERE, String.format(
						"CfgUtilities.initCacheFactory() call to CfgUtilities.createInstance(%s) FAILED", factory_cls));

				throw new InvalidClassException(factory_cls, "Class in not extended from CfgCacheFactory");
			}

			// Call out to the cacheFactory to initialize itself
			cacheFactory.init(factory_cls);
		}
	}

	/**
	 * <P>
	 * Utility Function to check if the string is an acceptable <tt>false</tt>
	 * value.
	 * 
	 * @param s
	 *            s is the string that will be checked
	 * @return Returns <tt>true</tt> if the string is an acceptable false value,
	 *         otherwise <tt>false</tt> will be returned
	 */
	public static boolean isCfgFalse(String s) {
		boolean ret_val = false;

		if (s != null && !s.isEmpty()
				&& (0 == s.compareToIgnoreCase("false") || 0 == s.compareToIgnoreCase("F"))) {
			ret_val = true;
		}
		return ret_val;
	}



	/**
	 * <P>
	 * Utility Function to check to if the logging level would be logged based
	 * on the system parameters
	 * 
	 * @param level
	 *            level specifies the logging Level
	 * @return Returns <tt>true</tt> if the logger is enable to log a message,
	 *         otherwise <tt>false</tt> will be returned
	 * 
	 */
	public static boolean IsLogLevelLoggable(Level level) {
		boolean bRtn = false;

		return bRtn;
	}

	/**
	 * <P>
	 * Utility Function to log a message to the Configurator Logger
	 * 
	 * @param level
	 *            level specifies the logging Level
	 * @param msg
	 *            msg specifies the logging message to be logged
	 */
	public static void logCfgMsg(Level level, String msg) {
		if (IsLogLevelLoggable(level)) {
		}
	}

	/**
	 * primeFolder() is a utility method that primes a specific folder location.
	 * If the location does not exist then the method will try and create it,
	 * otherwise if the location already exists it will purge any items that it
	 * contains
	 *
	 * @param folder
	 *            folder location that needs to be primed
	 * @return Boolean value indicating that no errors were detected
	 */
	public static boolean primeFolder(String folder) {
		boolean bRtn = true;

		File file = new File(folder);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				// "Failed to create folder
				bRtn = false;
			}
		} else if (!file.isDirectory()) {
			// "Failed to create folder since there is a file with the same name
			bRtn = false;
		}

		if (true == bRtn) {
			bRtn = purgeFolder(file.getAbsolutePath());
		}

		return bRtn;
	}

	/**
	 * purgeFolder() is a utility method that is used to remove any items that
	 * exists in the specified folder.
	 *
	 * @param folder
	 *            folder location that needs to be purged
	 * @return Boolean value indicating that no errors were detected
	 */
	public static boolean purgeFolder(String folder) {
		boolean bRtn = true;

		File files = new File(folder);
		for (File file : files.listFiles()) {
			if (file.isDirectory())
				purgeFolder(file.getAbsolutePath());
			try {
				bRtn = file.delete();
			} catch (Exception e) {
				bRtn = false;
			}

			// Exit the for loop if we can't purge the folder properly.
			if (!bRtn)
				break;
		}
		return bRtn;
	}

	/**
	 * <P>
	 * Utility Function to reset CfgUtilities static properties
	 * 
	 */
	protected static void reset() {
		synchronized (Utilities.class) {
			// See if we have an active cache factory instance
			if (cacheFactory != null) {
				String cacheType = Utilities.getResourceProperties().getProperty(Constants.CACHE_TYPE);
		
				synchronized (cacheFactory) {
					// Loop through all the caches and release them
					for (Entry<String, CacheInterface> entry : cacheFactory.getMapCaches().entrySet()) {
						CacheInterface cache = entry.getValue();
						if (cache != null) {
							try {
								cache.release();
							} catch (Exception e) {

							}
						}
					}

					// Clear the cache factory cache mapping
					cacheFactory.getMapCaches().clear();
				}
				cacheFactory = null;
			}

		}
	}

	/**
	 * <P>
	 * Utility Function to set the resource property file name. The default
	 * property file name is CfgConstants.CONFIGURATOR_PROPERTIES_FILE_NAME
	 * 
	 * <P>
	 * Setting of the resource property file will cause the resetting of the
	 * CfgUtilities static variables.
	 * 
	 * @param properties_file
	 *            Specifies the resource property file that will be used
	 */
	public static void setResourcePropertiesFileName(String properties_file) throws IOException {
		Utilities.reset();
		
		// Leverage Java7 try-with-resources to ensures resource is closed
		try (FileInputStream in = new FileInputStream(properties_file)) {
			Utilities.setResourceProperties(properties_file, in);
		}
	}

	/**
	 * <P>
	 * Utility Function to set the resource property file name. The default
	 * property file name is CfgConstants.CONFIGURATOR_PROPERTIES_FILE_NAME
	 * 
	 * @param properties
	 *            New resource {@link Properties} object that will be used
	 */
	public static void setLogLevel(Level iLevel) {
	}

	/**
	 * <P>
	 * Utility Function to set the resource property file name. The default
	 * property file name is CfgConstants.CONFIGURATOR_PROPERTIES_FILE_NAME
	 * 
	 * @param properties
	 *            New resource {@link Properties} object that will be used
	 */
	public static void setLevel(org.apache.logging.log4j.Level iLevel) {

	}

	/**
	 * <P>
	 * Utility Function to set the resource properties from an InputStream.
	 * 
	 * @param in
	 *            Specifies the InputStream that will be used
	 */
	protected static void setResourceProperties(String properties_file, InputStream in) throws IOException {
		synchronized (Utilities.class) {
			Utilities.properties_file = properties_file;
			Utilities.properties = new Properties();

			// default parameters

			try {
				Utilities.properties.load(in);
			} catch (IOException e) {
				// Generate a log message
				Utilities.logCfgMsg(Level.SEVERE, "CfgUtilities.setResourceProperties(InputStream) call -- FAILED");

				throw e;
			}

			// Generate a log message
			if (Utilities.IsLogLevelLoggable(Level.CONFIG)) {
				Utilities.logCfgMsg(Level.CONFIG,
						String.format("CfgUtilities.setResourceProperties(%s, InputStream)", properties_file));
			}
		}
	}



	public static boolean isInteger(String s) {
		boolean isInteger = true;
		try {
			Integer.parseInt(s);
		} catch (Exception ex) {
			isInteger = false;
		}
		return isInteger;
	}

	public static boolean isNumeric(String s) {
		boolean isNumeric = true;
		try {
			new BigDecimal(s);
		} catch (Exception ex) {
			isNumeric = false;
		}
		return isNumeric;
	}

	// This converts a date represented as a string into an integer that represents number of days
	public static int convertDateToInt(String strDate) {
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		long dateAsLong = 0;
		int dateAsInt = 0;
		Date inputDate = null;
		
		try {
			inputDate = dateFormatter.parse(strDate);
			Calendar dateGMT = new GregorianCalendar();
			dateGMT.setTimeZone(TimeZone.getDefault());
			dateGMT.setTime(inputDate);
			dateAsLong = dateGMT.getTimeInMillis();
			dateAsInt = (int) TimeUnit.DAYS.convert(dateAsLong, TimeUnit.MILLISECONDS);
		} catch (ParseException parseEx) {
			parseEx.printStackTrace();
		}

		return dateAsInt;
	}
	
	// This converts days represented as an integer into a date as a string
	public static String convertIntToDate(Integer intDate) {
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		long dateAsLong = 0;
		String dateAsString = null;

		Calendar dateGMT = new GregorianCalendar();
		dateGMT.setTimeZone(TimeZone.getDefault());
		dateAsLong = TimeUnit.MILLISECONDS.convert(Long.valueOf(intDate.longValue()), TimeUnit.DAYS);
		dateGMT.setTimeInMillis(dateAsLong);
		dateAsString = dateFormatter.format(dateGMT.getTime());
		
		return dateAsString;
	}
}
