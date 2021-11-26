package org.infinispan.inmemory.schema;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

@ProtoDoc("@Indexed")
public class PurchasedProductValue {
   private String name;
   private String country;

   @ProtoFactory
   public PurchasedProductValue(String name, String country) {
      this.name = name;
      this.country = country;
   }


   @ProtoField(1)
   @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = \"keyword\"))")
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @ProtoField(2)
   @ProtoDoc("@Field(index=Index.YES, analyze = Analyze.NO, store = Store.YES)")
   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

}
