package org.infinispan.retail;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
import org.infinispan.retail.model.CustomerCommand;
import org.infinispan.retail.model.RetailProduct;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/commands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommandsResource {
    private static final Logger LOGGER = Logger.getLogger(CommandsResource.class);


    @GET
    public List<CustomerCommand> getAll(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        if (page == null && size == null) {
            return CustomerCommand.listAll();
        }
        PanacheQuery<CustomerCommand> query = CustomerCommand.findAll();
        query.page(page, size);
        return query.list();
    }

}
