package org.infinispan.inmemory;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.api.query.Query;
import org.infinispan.inmemory.config.InmemoryCatalogueConfig;
import org.infinispan.inmemory.schema.PurchasedProductKey;
import org.infinispan.inmemory.schema.PurchasedProductValue;
import org.infinispan.inmemory.schema.RetailProductValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class InmemoryCatalogController {

    private final RemoteCache<Long, RetailProductValue> catalogue;
    private final RemoteCache<PurchasedProductKey, PurchasedProductValue> soldProducts;
    private final RemoteCacheManager cacheManager;

    public InmemoryCatalogController(InmemoryCatalogueConfig config,
                                     ConfigureSQLStore configureSQLStore,
                                     RemoteCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        configureSQLStore.onStart();
        this.catalogue = cacheManager.getCache(config.getCatalogCacheName());
        this.soldProducts = cacheManager.getCache(config.getSoldProductsName());
    }

    @GetMapping("health")
    public String health() {
        return String.format("Service is up! catalogue[%d] sold_products[%d]",
                catalogue.size(), soldProducts.size());
    }

    @GetMapping("reindex")
    public String reindex() {
        cacheManager.administration().reindexCache(catalogue.getName());
        cacheManager.administration().reindexCache(soldProducts.getName());
        return "Reindex launched";
    }

    @GetMapping("catalogue/{code}")
    public ResponseEntity<RetailProductValue> getByCode(@PathVariable String code) {
        String queryByCode = "from retail.RetailProductValue where code= :code";
        Query<RetailProductValue> query = catalogue.query(queryByCode);
        query.setParameter("code", code);
        query.maxResults(1);

        List<RetailProductValue> list = query.execute().list();
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(list.get(0));
    }

    @GetMapping("catalogue")
    public ResponseEntity<List<RetailProductValue>> queryCatalogue(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "stock") Integer stock,
            @RequestParam(required = false, name = "price-max") Integer priceMax,
            @RequestParam(required = false, name = "price-min") Integer priceMin) {

        if (name == null) {
            name = "*";
        } else {
            name = "*" + name + "*";
        }

        StringBuilder queryBuilder = new StringBuilder(
                String.format("from retail.RetailProductValue where name: '%s'", name));

        if (stock != null) {
            queryBuilder.append(String.format(" and stock >= %d", stock));
        }

        if (priceMin != null && priceMax != null) {
            queryBuilder.append(String.format(" and price : [%d to %d]", priceMin, priceMax));
        }

        Query<RetailProductValue> query = catalogue.query(queryBuilder.toString());
        query.maxResults(Integer.MAX_VALUE); // TODO: use pagination

        return ResponseEntity.ok(query.execute().list());
    }

    @GetMapping("sales")
    public ResponseEntity<List<PurchasedProductValue>> querySales(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String country) {
        if (name == null) {
            name = "*";
        } else {
            name = "*" + name + "*";
        }

        StringBuilder queryBuilder = new StringBuilder(
                String.format("from retail.PurchasedProductValue where name: '%s'", name));

        if (country != null) {
            queryBuilder.append(String.format(" and country='%s'", country));
        }

        Query<PurchasedProductValue> query = soldProducts.query(queryBuilder.toString());
        query.maxResults(Integer.MAX_VALUE); // TODO: use pagination

        return ResponseEntity.ok(query.execute().list());
    }
}
