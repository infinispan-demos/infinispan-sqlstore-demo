package org.infinispan.retail;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.infinispan.retail.model.CustomerCommand;
import java.util.List;

@ApplicationScoped
@Path("/commands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommandsResource {
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
