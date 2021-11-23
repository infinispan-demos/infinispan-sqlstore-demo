package org.infinispan.inmemory;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InmemoryCatalogResource {
    private static final Logger LOGGER = Logger.getLogger(InmemoryCatalogResource.class);

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Infinispan SQL Store is starting Powered by Quarkus");
        LOGGER.info("  _   _   _   _   _   _   _   _");
        LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
        LOGGER.info("( S | q | l | S | t | o | r | e )");
        LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
    }

    @GET
    public Response catalog() {
        return Response.ok("in progress").build();
    }


}
