package org.infinispan.inmemory;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.commons.api.query.Query;
import org.infinispan.inmemory.config.InmemoryCatalogueConfig;
import org.infinispan.inmemory.schema.PurchasedProductKey;
import org.infinispan.inmemory.schema.PurchasedProductValue;
import org.infinispan.inmemory.schema.RetailProductValue;
import java.util.List;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class InmemoryCatalogResource {

    final RemoteCache<Long, RetailProductValue> catalogue;
    final RemoteCache<PurchasedProductKey, PurchasedProductValue> soldProducts;
    final RemoteCacheManager cacheManager;

    @Inject
    public InmemoryCatalogResource(InmemoryCatalogueConfig inmemoryCatalogueConfig, RemoteCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.catalogue = cacheManager.getCache(inmemoryCatalogueConfig.catalogCacheName());
        this.soldProducts = cacheManager.getCache(inmemoryCatalogueConfig.soldProductsName());
    }

    @GET
    @Path("health")
    public String health() {
        int catalogueSize = 0;
        int soldProductsSize = 0;

        try {
            catalogueSize = catalogue.size();
        } catch (Exception ex) {
            Log.error("Error getting catalogue size", ex);
        }

        try {
            soldProductsSize = soldProducts.size();
        } catch (Exception ex) {
            Log.error("Error getting sold products size", ex);
        }

        return String.format("Service is up! catalogue[%d] sold_products[%d]", catalogueSize, soldProductsSize);
    }

    @GET
    @Path("reindex")
    public String reindex() {
        cacheManager.administration().reindexCache(catalogue.getName());
        cacheManager.administration().reindexCache(soldProducts.getName());
        return String.format("Reindex launched");
    }

    @GET
    @Path("catalogue/{code}")
    public Response getByCode(@PathParam("code") String code) {
        Query<RetailProductValue> query = catalogue.query("from retail.RetailProductValue where code= :code");
        query.setParameter("code", code);
        query.maxResults(1);
        List<RetailProductValue> list = query.execute().list();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(list.get(0)).build();
    }

    @GET
    @Path("catalogue")
    public Response queryCatalogue(@QueryParam("name") String name,
                                   @QueryParam("stock") Integer stock,
                                   @QueryParam("price-max") Integer priceMax,
                                   @QueryParam("price-min") Integer priceMin) {
        if (name == null) {
            name = "*";
        } else {
            name = "*" +  name + "*";

        }
        String fullTextQuery = String.format("from retail.RetailProductValue where name: '%s'", name);

        if (stock != null) {
            fullTextQuery = fullTextQuery + String.format(" and stock >= %d", stock);
        }

        if (priceMin != null && priceMax != null) {
            fullTextQuery = fullTextQuery + String.format(" and price : [%d to %d]", priceMin, priceMax);
        }

        Query<RetailProductValue> query = catalogue.query(fullTextQuery);
        query.maxResults(Integer.MAX_VALUE); // all. TODO: use pagination
        return Response.ok(query.execute().list()).build();
    }

    @GET
    @Path("sales")
    public Response querySales(@QueryParam("name") String name, @QueryParam("country") String country) {
        if (name == null) {
            name = "*";
        } else {
            name = "*" + name + "*";
        }

        String fullTextQuery = "from retail.PurchasedProductValue where name: '" + name + "'";

        if (country != null) {
            fullTextQuery = fullTextQuery + "and country='" +  country +"'";
        }
        Query<RetailProductValue> query = soldProducts.query(fullTextQuery);
        query.maxResults(Integer.MAX_VALUE); // TODO: All. Use pagination
        return Response.ok(query.execute().list()).build();
    }

}
