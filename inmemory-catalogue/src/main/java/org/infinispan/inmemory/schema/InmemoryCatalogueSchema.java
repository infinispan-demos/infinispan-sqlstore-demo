package org.infinispan.inmemory.schema;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(includeClasses= { RetailProductValue.class, PurchasedProductKey.class, PurchasedProductValue.class},
      schemaPackageName = "retail")
public interface InmemoryCatalogueSchema extends GeneratedSchema {
}
