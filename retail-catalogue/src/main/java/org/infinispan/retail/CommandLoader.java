package org.infinispan.retail;

import org.infinispan.retail.model.Customer;
import org.infinispan.retail.model.CustomerCommand;
import org.infinispan.retail.model.RetailProduct;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class CommandLoader {

    private Random random = new Random();

    @Transactional
    public void clearCommands() {
        CustomerCommand.deleteAll();
    }

    @Transactional
    public void createCommand() {
        List<RetailProduct> products = RetailProduct.listAll();
        List<Customer> customers = Customer.listAll();
        CustomerCommand customerCommand = new CustomerCommand();
        customerCommand.promotion = true;
        customerCommand.buyer = customers.get(random.nextInt(customers.size()));
        int randomPos2 = random.nextInt(products.size());
        int randomPos1 = random.nextInt(randomPos2 + 1);
        customerCommand.products = products.subList(randomPos1, randomPos2);
        customerCommand.persistAndFlush();
    }

}
