package org.infinispan.retail;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.infinispan.retail.model.RetailProduct;
import java.util.List;

@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RetailCatalogResource {
    @Inject
    CommandLoader commandLoader;

    void onStart(@Observes StartupEvent ev) {
        Log.info("Retail Store is starting Powered by Quarkus");
        Log.info("  _   _   _   _   _   _");
        Log.info(" / \\ / \\ / \\ / \\ / \\ / \\");
        Log.info("( R | e | t | a | i | l )");
        Log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
        commandLoader.clearCommands();
        for (int i = 0 ; i < 20; i++) {
            commandLoader.createCommand();
        }
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
    public Response update(@PathParam("code") String code, RetailProduct retailProduct) {
        RetailProduct existingProduct = RetailProduct.findByCode(code);
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
