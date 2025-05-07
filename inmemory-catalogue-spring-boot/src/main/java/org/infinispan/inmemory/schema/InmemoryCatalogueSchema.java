package org.infinispan.inmemory.schema;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(
        schemaPackageName = "retail",
        includeClasses= {
                RetailProductValue.class,
                PurchasedProductKey.class,
                PurchasedProductValue.class
        })
public interface InmemoryCatalogueSchema extends GeneratedSchema {
}
