package org.infinispan.inmemory;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.inmemory.config.InmemoryCatalogueConfig;
import org.infinispan.inmemory.schema.PurchasedProductKey;
import org.infinispan.inmemory.schema.PurchasedProductValue;
import org.infinispan.inmemory.schema.RetailProductValue;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
        return String.format("Service is up! catalogue[%d] sold_products[%d]", catalogue.size(), soldProducts.size());
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
        String queryByCode = "from retail.RetailProductValue where code= :code";
        QueryFactory queryFactory = Search.getQueryFactory(catalogue);
        Query<RetailProductValue> query = queryFactory.create(queryByCode);
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

        QueryFactory queryFactory = Search.getQueryFactory(catalogue);
        Query<RetailProductValue> query = queryFactory.create(fullTextQuery);
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
        QueryFactory queryFactory = Search.getQueryFactory(soldProducts);
        Query<RetailProductValue> query = queryFactory.create(fullTextQuery);
        query.maxResults(Integer.MAX_VALUE); // TODO: All. Use pagination
        return Response.ok(query.execute().list()).build();
    }

}
