package org.infinispan.retail;

import org.infinispan.retail.model.Customer;
import org.infinispan.retail.model.CustomerCommand;
import org.infinispan.retail.model.RetailProduct;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CommandLoader {

    private static final Logger LOGGER = Logger.getLogger(CommandLoader.class);

    @Transactional
    public void initCommands() {
        LOGGER.info("Creating commands");
        List<RetailProduct> products = RetailProduct.listAll();
        List<Customer> customers = Customer.listAll();

        CustomerCommand customerCommand = new CustomerCommand();
        customerCommand.promotion = true;
        customerCommand.buyer = customers.get(0);
        customerCommand.products = products;

        customerCommand.persist();
    }

}
