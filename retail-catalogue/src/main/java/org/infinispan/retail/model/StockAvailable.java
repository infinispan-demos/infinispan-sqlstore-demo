package org.infinispan.retail.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StockAvailable {
   public final int stock;

   public StockAvailable(int stock) {
      this.stock = stock;
   }
}
