package org.infinispan.inmemory.schema;

import org.infinispan.protostream.annotations.Proto;
import org.infinispan.protostream.annotations.ProtoField;

@Proto
public class PurchasedProductKey {
   @ProtoField(number = 1, name = "id")
   Long id;
   @ProtoField(number = 2, name = "products_id")
   Long productId;

   public Long getId() {
      return id;
   }

   public Long getProductId() {
      return productId;
   }
}
