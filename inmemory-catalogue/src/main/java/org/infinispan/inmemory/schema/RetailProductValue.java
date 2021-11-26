package org.infinispan.inmemory.schema;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

@ProtoDoc("@Indexed")
public class RetailProductValue {
   private String code;
   private String name;
   private Double price;
   private Integer stock;

   @ProtoFactory
   public RetailProductValue(String code, String name, Double price, Integer stock) {
      this.code = code;
      this.name = name;
      this.price = price;
      this.stock = stock;
   }

   @ProtoField(1)
   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   @ProtoField(2)
   @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES)")
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @ProtoField(3)
   @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.NO, store = Store.YES)")
   public Double getPrice() {
      return price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   @ProtoField(4)
   @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.NO, store = Store.YES)")
   public Integer getStock() {
      return stock;
   }

   public void setStock(Integer stock) {
      this.stock = stock;
   }

   @Override
   public String toString() {
      return "RetailProductValue{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", price=" + price + ", stock="
            + stock + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      RetailProductValue that = (RetailProductValue) o;
      return Objects.equals(code, that.code) && Objects.equals(name, that.name)
            && Objects.equals(price, that.price) && Objects.equals(stock, that.stock);
   }

   @Override
   public int hashCode() {
      return Objects.hash(code, name, price, stock);
   }
}
