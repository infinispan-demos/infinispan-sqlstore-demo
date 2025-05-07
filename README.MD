[![Infinispan](https://design.jboss.org/infinispan/logo/final/PNG/infinispan_logo_rgb_lightbluewhite_darkblue_600px.png)](https://infinispan.org/)

# Infinispan SQL Store Demo with Spring-Boot and Quarkus # 

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

There are two implementations of the service:
* Spring Boot 3 version
* Quarkus version

#### Overall Description

Inmemory Catalogue creates two SQL Cache Stores with Infinispan on startup. 
1. `catalogue-table-store` is a table sql store persisted cache that maps to the RetailProduct table
2. `sold-products-query-store`is a query sql store persisted cache that joins information across multiple tables.

Both caches use indexing.

The application exposes 3 endpoints:
* `catalogue`: Lists the catalogue content. Can filter by name, stock units and prices using query parameters.
* `catalogue/{code}`: Displays the catalogue product by code
* `sales`: Lists the  products than have been sold. Filter can be done name of the product and country.

## Dev mode 

* Run Infinispan and Postgresql in the same network with docker

```shell
docker-compose up
```
Dev services are disabled since the Infinispan and Postresql need to be in the same
docker network and Infinispan access the Postgresql database directly to load the data
into the caches.

* Run Retail Catalogue and Store

```shell
cd infinispan-sqlstore-demo/retail-catalogue 
./mvnw quarkus:dev 
```

* Run Infinispan In-memory catalogue
```shell
cd infinispan-sqlstore-demo/inmemory-catalogue 
./mvnw quarkus:dev 
```

## Links

Additional resources and useful links:

* [Infinispan Query and Indexing Guide](https://infinispan.org/docs/stable/titles/query/query.htm)
* [Infinispan SQL Cache Storage Documentation](https://infinispan.org/docs/stable/titles/configuring/configuring.html#sql-cache-store_persistence)
* [Quarkus Infinispan Extension](https://quarkus.io/guides/infinispan-client)

## Deploy Minikube

1. Install and start Minikube
```shell
 minikube start --driver=virtualbox --cpus 4 --memory "8192mb"
```   
2. Create Kubernetes Secret `sqlstore-credentials`
```shell
kubectl create secret generic clients-credentials 
--from-literal=db-username=infinispan 
--from-literal=db-password=secret
--from-literal=infinispan-username=admin 
--from-literal=infinispan-password=secret
```

3. Deploy Postgresql
```shell
kubectl apply -f postgres.yaml
```

4. Build and deploy Retail Catalogue
```shell
eval $(minikube -p minikube docker-env)
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true 

# Export the service URL 
export RETAIL_CATALOGUE=$(minikube service --url retail-store-service) 

# Check the commands endpoint 
curl $RETAIL_CATALOGUE/commands
```

5. Install the Infinispan Operator
```shell
curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.19.1/install.sh | bash -s v0.19.1
kubectl create -f https://operatorhub.io/install/infinispan.yaml
kubectl get csv -n operators
```

You should see
```shell
NAME                         DISPLAY               VERSION   REPLACES                     PHASE
infinispan-operator.v2.2.1   Infinispan Operator   2.2.1     infinispan-operator.v2.2.0   Succeeded
```

6. Create Infinispan identities secret
```shell
kubectl create secret generic --from-file=identities.yaml connect-secret
```

7. Deploy infinispan server
```shell
kubectl apply -f infinispan.yaml
```

You should see something like the following list

```shell
 minikube service list    
                                                                                    15:17:03
|-------------|-----------------------|--------------|-----------------------------|
|  NAMESPACE  |         NAME          | TARGET PORT  |             URL             |
|-------------|-----------------------|--------------|-----------------------------|
| default     | infinispan            | No node port |
| default     | infinispan-admin      | No node port |
| default     | infinispan-external   |        11222 | http://192.168.59.101:30000 |
| default     | infinispan-ping       | No node port |
| default     | kubernetes            | No node port |
| default     | postgres              | http/5432    | http://192.168.59.101:31684 |
| default     | retail-store-service  | http/80      | http://192.168.59.101:31056 |
| kube-system | kube-dns              | No node port |
| olm         | operatorhubio-catalog | No node port |
| olm         | packageserver-service | No node port |
|-------------|-----------------------|--------------|-----------------------------|

```

8. Build and deploy Inmemory Catalogue
```shell
eval $(minikube -p minikube docker-env)
./mvnw clean package -Dquarkus.kubernetes.deploy=true -DskipTests=true 

# Export the service URL 
export INMEMORY_CATALOGUE=$(minikube service --url inmemory-catalogue-service) 

curl $INMEMORY_CATALOGUE
```