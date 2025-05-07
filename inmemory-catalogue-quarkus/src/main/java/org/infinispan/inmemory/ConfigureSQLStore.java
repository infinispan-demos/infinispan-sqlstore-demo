package org.infinispan.inmemory;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.StringConfiguration;
import org.infinispan.inmemory.config.InmemoryCatalogueConfig;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class ConfigureSQLStore {
   private static final Logger LOGGER = Logger.getLogger(ConfigureSQLStore.class);

   @Inject
   RemoteCacheManager remoteCacheManager;

   @Inject
   InmemoryCatalogueConfig inmemoryCatalogueConfig;

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("Infinispan SQL Store is starting Powered by Quarkus");
      LOGGER.info("  _   _   _   _   _   _   _   _");
      LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      LOGGER.info("( S | q | l | S | t | o | r | e )");
      LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
      try {
         LOGGER.info("Create caches");
         URL tableStoreCacheConfig = this.getClass().getClassLoader().getResource("tableStore.xml");
         URL queryStoreCacheConfig = this.getClass().getClassLoader().getResource("queryStore.xml");
         String configTable = replaceDBConnectionConfiguration(tableStoreCacheConfig);
         String configQuery = replaceDBConnectionConfiguration(queryStoreCacheConfig);
         remoteCacheManager.administration()
               .getOrCreateCache(inmemoryCatalogueConfig.catalogCacheName(), new StringConfiguration(configTable));
         remoteCacheManager.administration()
               .getOrCreateCache(inmemoryCatalogueConfig.soldProductsName(), new StringConfiguration(configQuery));
      } catch (Exception e) {
         LOGGER.error("Something went wrong creating caches");
      }
   }

   private String replaceDBConnectionConfiguration(URL cacheConfig) throws IOException {
      String config = IOUtils.toString(cacheConfig, StandardCharsets.UTF_8)
                  .replace("CONNECTION_URL", inmemoryCatalogueConfig.connectionUrl())
                  .replace("USER_NAME", inmemoryCatalogueConfig.userName())
                  .replace("PASSWORD", inmemoryCatalogueConfig.password())
                  .replace("DIALECT", inmemoryCatalogueConfig.dialect())
                  .replace("DRIVER", inmemoryCatalogueConfig.driver());

      return config;
   }

}
