package org.infinispan.inmemory.schema;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.api.annotations.indexing.Text;
import org.infinispan.protostream.annotations.Proto;

@Proto
@Indexed
public record PurchasedProductValue(@Text
                                    String name,
                                    @Text
                                    String country) {

}
