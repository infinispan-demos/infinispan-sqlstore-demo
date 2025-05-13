package org.infinispan.retail.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.List;

@RegisterForReflection
@Entity
public class CustomerCommand extends PanacheEntity {
   public Boolean promotion;

   @ManyToOne(fetch = FetchType.LAZY)
   public Customer buyer;

   @ManyToMany(fetch = FetchType.LAZY)
   public List<RetailProduct> products;

}
