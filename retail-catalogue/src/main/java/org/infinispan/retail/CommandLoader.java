package org.infinispan.retail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.infinispan.retail.model.Customer;
import org.infinispan.retail.model.CustomerCommand;
import org.infinispan.retail.model.RetailProduct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class CommandLoader {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    @Transactional
    public void clearCommands() {
        CustomerCommand.deleteAll();
    }

    @Transactional
    public void createCommand() {
        List<Customer> customers = Customer.listAll();
        List<RetailProduct> products = RetailProduct.listAll();
        CustomerCommand customerCommand = new CustomerCommand();
        customerCommand.promotion = true;
        customerCommand.buyer = customers.get(random.nextInt(customers.size()));
        int randomPos2 = random.nextInt(products.size());
        int randomPos1 = random.nextInt(randomPos2 + 1);
        customerCommand.products = products.subList(randomPos1, randomPos2);
        customerCommand.persistAndFlush();
    }

}
