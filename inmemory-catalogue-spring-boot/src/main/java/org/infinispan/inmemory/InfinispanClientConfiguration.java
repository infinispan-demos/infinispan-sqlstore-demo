package org.infinispan.inmemory;

import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.inmemory.schema.InmemoryCatalogueSchemaImpl;
import org.infinispan.spring.starter.remote.InfinispanRemoteCacheCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class InfinispanClientConfiguration {

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   public InfinispanRemoteCacheCustomizer caches() {
      return b -> {
         // Add marshaller in the client, the class is generated from the interface in compile time
         b.addContextInitializer(new InmemoryCatalogueSchemaImpl());
         // Java Serialization is default in Spring Boot. Use Protostream to use query
         b.marshaller(ProtoStreamMarshaller.class);
      };
   }
}
