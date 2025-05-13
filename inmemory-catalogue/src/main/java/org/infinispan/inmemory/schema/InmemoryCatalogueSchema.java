package org.infinispan.inmemory.schema;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses= { RetailProductValue.class, PurchasedProductKey.class, PurchasedProductValue.class},
      schemaPackageName = "retail")
public interface InmemoryCatalogueSchema extends GeneratedSchema {
}
