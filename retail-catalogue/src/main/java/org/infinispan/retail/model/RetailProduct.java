package org.infinispan.retail.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.Objects;

@RegisterForReflection
@Entity
public class RetailProduct extends PanacheEntity {

   public String code;
   public String name;
   public BigDecimal price;
   public int stock;

   public static RetailProduct findByCode(String code) {
      return find("code", code)
            .<RetailProduct>firstResultOptional()
            .orElseThrow(()-> new NotFoundException());
   }

   public static int findCurrentStock(String code) {
      StockAvailable stockAvailable = find("code", code)
            .project(StockAvailable.class).firstResultOptional()
            .orElseThrow(() -> new NotFoundException());

      return stockAvailable.stock;
   }

   @Override
   public String toString() {
      return "RetailProduct{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", price=" + price + ", stock="
            + stock + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      RetailProduct that = (RetailProduct) o;
      return stock == that.stock && Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects
            .equals(price, that.price);
   }

   @Override
   public int hashCode() {
      return Objects.hash(code, name, price, stock);
   }
}
