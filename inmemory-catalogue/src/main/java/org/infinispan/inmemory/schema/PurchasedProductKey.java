package org.infinispan.inmemory.schema;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

public class PurchasedProductKey {
   private Long commandId;
   private Long productId;

   @ProtoFactory
   public PurchasedProductKey(Long commandId, Long productId) {
      this.commandId = commandId;
      this.productId = productId;
   }

   @ProtoField(number = 1, name = "id")
   public Long getCommandId() {
      return commandId;
   }

   public void setCommandId(Long commandId) {
      this.commandId = commandId;
   }

   @ProtoField(number = 2, name = "products_id")
   public Long getProductId() {
      return productId;
   }

   public void setProductId(Long productId) {
      this.productId = productId;
   }
}
