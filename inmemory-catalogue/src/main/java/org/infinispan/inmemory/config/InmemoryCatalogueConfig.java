package org.infinispan.inmemory.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "inmemory")
public interface InmemoryCatalogueConfig {

   String connectionUrl();

   @WithName("username")
   String userName();

   String password();

   String dialect();

   String driver();

   @WithDefault("catalogue-table-store")
   String catalogCacheName();

   @WithDefault("sold-products-query-store")
   String soldProductsName();
}
