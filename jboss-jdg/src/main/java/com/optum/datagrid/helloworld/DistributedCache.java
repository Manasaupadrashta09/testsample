package com.optum.datagrid.helloworld;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*import java.util.logging.Level;
import java.util.logging.Logger;*/
import org.apache.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.NearCacheMode;

/**
 *
 * @author mupadras
 */
public class DistributedCache {
	private static final String ENV_VAR_JDG_SERVICE_NAME = "JDG_SERVICE_NAME";
	private static final String ENV_VAR_SUFFIX_HOTROD_SERVICE_PORT = "_HOTROD_SERVICE_PORT";
	private static final String ENV_VAR_SUFFIX_HOTROD_SERVICE_HOST = "_HOTROD_SERVICE_HOST";

	final static Logger logger = Logger.getLogger(DistributedCache.class);

    public RemoteCacheManager getCacheManager() throws Exception {
    	org.infinispan.client.hotrod.configuration.ConfigurationBuilder builder = new org.infinispan.client.hotrod.configuration.ConfigurationBuilder();
	//Another way to addserver to builder
        //.addServer().host("localhost").port(11322);
    
        logger.info("*** In the method get Cache Manager ***");
        builder
			.nearCache()
				.mode(NearCacheMode.LAZY)
				.maxEntries(500)
			.addServer()
			.host("10.1.29.3")
			.port(11333);
        System.out.println(" ## Just making sure ##");
        System.out.println(" printing Host ");

			return new RemoteCacheManager(builder.build(), true);
    }
    
    private static String getHotRodHostFromEnvironment() throws DataGridConfigurationException {
		String hotrodServiceName = System.getenv(ENV_VAR_JDG_SERVICE_NAME);
		if(hotrodServiceName == null) {
			throw new DataGridConfigurationException("Failed to get JDG Service Name from environment variables. please make sure that you set this value before starting the container");
		}
		String hotRodHost=System.getenv(hotrodServiceName.toUpperCase() + ENV_VAR_SUFFIX_HOTROD_SERVICE_HOST);
		if(hotRodHost == null) {
			throw new DataGridConfigurationException(String.format("Failed to get hostname/ip address for service %s",hotrodServiceName));
		}
		return hotRodHost;
	}
	
	private static int getHotRodPortFromEnvironment() throws DataGridConfigurationException {
		String hotrodServiceName = System.getenv(ENV_VAR_JDG_SERVICE_NAME);
		if(hotrodServiceName == null) {
			throw new DataGridConfigurationException("Failed to get JDG Service Name from environment variables. please make sure that you set this value before starting the container");
		}
		String hotRodPort=System.getenv(hotrodServiceName.toUpperCase() + ENV_VAR_SUFFIX_HOTROD_SERVICE_PORT);
		if(hotRodPort == null) {
			throw new DataGridConfigurationException(String.format("Failed to get Hot Rod Port for service %s",hotrodServiceName));
		}
		return Integer.parseInt(hotRodPort);
	}
	
	public static class DataGridConfigurationException extends Exception
    {
		private static final long serialVersionUID = -4667039447165906505L;
		public DataGridConfigurationException(String msg) {
            super(msg);
        }
    }
    
    

    public void saveData(String inputKey, String inputValue) {
        // Setup up a clustered cache manager
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        // Make the default cache a distributed synchronous one
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(CacheMode.DIST_SYNC);
        // Initialize the cache manager
        RemoteCacheManager cacheManager = null;
        try {
            System.out.println(" ## Message1 ##");
            cacheManager = getCacheManager();
        } catch (Exception ex) {
           // Logger.getLogger(DistributedCache.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error("Error occured:"+ex.getMessage());
        }
        System.out.println(" ## Message2 ##");

        // Obtain the default cache
        if(cacheManager!=null){
        RemoteCache<String, String> cache = cacheManager.getCache();
        // Store the current node address in some random keys. 
        System.out.println("Data Saving to Cache.......");
        cache.put("value", "value");
        System.out.println(" ## Message3 ##");


        cacheManager.stop();
    }}

    public void readData(String cacheName) {
        RemoteCacheManager cacheManager = null;
        try {
            System.out.println(" ## Message4 ##");

            cacheManager = getCacheManager();
        } catch (Exception ex) {
            //Logger.getLogger(DistributedCache.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error("Error occured:"+ex.getMessage());
        }
        if(cacheManager!=null){
        // Obtain the default cache
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);
        System.out.println(cache.values());
        cacheManager.stop();
    }
}}