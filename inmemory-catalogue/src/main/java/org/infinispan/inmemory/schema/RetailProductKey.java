package org.infinispan.inmemory.schema;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

public class RetailProductKey {
   private long id;

   @ProtoFactory
   public RetailProductKey(long id) {
      this.id = id;
   }

   @ProtoField(value = 1, required = true)
   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return "RetailProductKey{" + "id=" + id + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      RetailProductKey that = (RetailProductKey) o;
      return Objects.equals(id, that.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }
}
