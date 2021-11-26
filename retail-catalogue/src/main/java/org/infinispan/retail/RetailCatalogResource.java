package org.infinispan.retail;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
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
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RetailCatalogResource {
    private static final Logger LOGGER = Logger.getLogger(RetailCatalogResource.class);

    @Inject
    CommandLoader commandLoader;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Retail Store is starting Powered by Quarkus");
        LOGGER.info("  _   _   _   _   _   _");
        LOGGER.info(" / \\ / \\ / \\ / \\ / \\ / \\");
        LOGGER.info("( R | e | t | a | i | l )");
        LOGGER.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
        commandLoader.initCommands();
    }

    @GET
    public List<RetailProduct> catalog(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        if (page == null && size == null) {
            return RetailProduct.listAll();
        }
        PanacheQuery<RetailProduct> query = RetailProduct.findAll(Sort.by("name"));
        query.page(page, size);
        return query.list();
    }

    @GET
    @Path("/{code}")
    public Response getRetailProductByCode(@PathParam("code") String code) {
        RetailProduct retailProduct = RetailProduct.findByCode(code);
        return Response.ok(retailProduct).build();
    }

    @POST
    @Transactional
    public Response create(RetailProduct retailProduct, @Context UriInfo uriInfo) {
        retailProduct.persist();
        return Response.created(uriInfo.getAbsolutePathBuilder().path(retailProduct.code).build()).build();
    }

    @PUT
    @Path("/{code}")
    @Transactional
    public Response update(@PathParam("code") String sku, RetailProduct retailProduct) {
        RetailProduct existingProduct = RetailProduct.findByCode(sku);
        existingProduct.name = retailProduct.name;
        existingProduct.price = retailProduct.price;
        existingProduct.stock = retailProduct.stock;
        existingProduct.persist();
        return Response.ok(existingProduct).build();
    }

    @PATCH
    @Path("/{code}")
    @Transactional
    public Response updateStock(@PathParam("code") String code, @QueryParam("stock") Integer stock) {
        int currentStock = RetailProduct.findCurrentStock(code);
        RetailProduct.update("stock = ?1 where code= ?2", currentStock + stock, code);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{code}")
    @Transactional
    public Response delete(@PathParam("code") String code) {
        RetailProduct.delete("code", code);
        return Response.ok().build();
    }

}
