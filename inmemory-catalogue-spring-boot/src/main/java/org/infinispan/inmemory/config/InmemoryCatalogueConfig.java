package org.infinispan.inmemory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "inmemory")
public class InmemoryCatalogueConfig {

   private String connectionUrl;
   private String username;
   private String password;
   private String dialect;
   private String driver;
   private String catalogCacheName = "catalogue-table-store";
   private String soldProductsName = "sold-products-query-store";

   // Getters and Setters

   public String getConnectionUrl() {
      return connectionUrl;
   }

   public void setConnectionUrl(String connectionUrl) {
      this.connectionUrl = connectionUrl;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getDialect() {
      return dialect;
   }

   public void setDialect(String dialect) {
      this.dialect = dialect;
   }

   public String getDriver() {
      return driver;
   }

   public void setDriver(String driver) {
      this.driver = driver;
   }

   public String getCatalogCacheName() {
      return catalogCacheName;
   }

   public void setCatalogCacheName(String catalogCacheName) {
      this.catalogCacheName = catalogCacheName;
   }

   public String getSoldProductsName() {
      return soldProductsName;
   }

   public void setSoldProductsName(String soldProductsName) {
      this.soldProductsName = soldProductsName;
   }
}
