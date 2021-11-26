package org.infinispan.retail.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;

@RegisterForReflection
@Entity
public class Customer extends PanacheEntity {

   public String name;
   public String email;
   public String phone;
   public String country;

}
