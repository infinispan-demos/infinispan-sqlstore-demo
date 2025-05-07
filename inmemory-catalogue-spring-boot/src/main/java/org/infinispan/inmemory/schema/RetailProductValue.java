package org.infinispan.inmemory.schema;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.protostream.annotations.Proto;

@Proto
@Indexed
public record RetailProductValue(@Basic String code,
                                 @Basic String name,
                                 @Basic Double price,
                                 @Basic Integer stock) {

}