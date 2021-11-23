package org.infinispan.inmemory.schema;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.types.java.math.BigDecimalAdapter;

@AutoProtoSchemaBuilder(includeClasses= { RetailProductKey.class, RetailProductValue.class, BigDecimalAdapter.class },
      schemaPackageName = "retail")
public interface InmemoryCatalogueSchema extends GeneratedSchema {
}
