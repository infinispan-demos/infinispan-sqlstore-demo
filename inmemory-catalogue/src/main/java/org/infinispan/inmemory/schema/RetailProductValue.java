package org.infinispan.inmemory.schema;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.api.annotations.indexing.Keyword;
import org.infinispan.protostream.annotations.Proto;

@Proto
@Indexed
public record RetailProductValue(String code,
                                 @Keyword
                                 String name,
                                 @Basic
                                 Double price,
                                 @Basic
                                 Integer stock) {

}
