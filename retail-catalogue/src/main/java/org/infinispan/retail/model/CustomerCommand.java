package org.infinispan.retail.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@RegisterForReflection
@Entity
public class CustomerCommand extends PanacheEntity {
   public Boolean promotion;

   @OneToOne(fetch = FetchType.LAZY)
   public Customer buyer;

   @OneToMany(fetch = FetchType.LAZY)
   public List<RetailProduct> products;

}
