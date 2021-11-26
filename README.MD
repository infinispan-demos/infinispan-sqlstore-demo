[![Infinispan](https://design.jboss.org/infinispan/logo/final/PNG/infinispan_logo_rgb_lightbluewhite_darkblue_600px.png)](https://infinispan.org/)

# Infinispan SQL Store Demo with Quarkus # 

## Description

### Retail Catalogue

Retail Catalogue is a very simple Quarkus application that persists data in a Postgresql database. 

'commands' endpoint displays the content of the database model.
```json
[
    {
        "id": 5,
        "buyer": {
            "id": 4,
            "country": "Spain",
            "email": "mfrechilla@quarkus.io",
            "name": "Maria Rosario Frechilla",
            "phone": "+34 666"
        },
    "products": [
            {
                "id": 1,
                "code": "c123",
                "name": "Skirt Party",
                "price": 50,
                "stock": 20
            },
            {
                "id": 2,
                "code": "c456",
                "name": "Pants Party",
                "price": 20,
                "stock": 10
            },
            {
                "id": 3,
                "code": "c789",
                "name": "Dress Party",
                "price": 90,
                "stock": 20
            }
            ],
          "promotion": true
        }
]
```

### Inmemory Catalogue

Inmemory Catalogue creates two SQL Cache Stores with Infinispan on startup. 
1. `catalogue-table-store` is a table sql store persisted cache that maps to the RetailProduct table
2. `sold-products-query-store`is a query sql store persisted cache that joins information across multiple tables.

Both caches use indexing.

The application exposes 3 endpoints:
* `catalogue`: Lists the catalogue content. Can filter by name, stock units and prices using query parameters.
* `catalogue/{code}`: Displays the catalogue product by code
* `sales`: Lists the  products than have been sold. Filter can be done name of the product and country.

## Links

Additional resources and useful links:

* [Infinispan Query and Indexing Guide](https://infinispan.org/docs/stable/titles/query/query.htm)
* [Infinispan SQL Cache Storage Documentation](https://infinispan.org/docs/stable/titles/configuring/configuring.html#sql-cache-store_persistence)
* [Quarkus Infinispan Extension](https://quarkus.io/guides/infinispan-client)
