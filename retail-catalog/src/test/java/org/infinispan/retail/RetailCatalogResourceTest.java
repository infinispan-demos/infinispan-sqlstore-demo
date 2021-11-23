package org.infinispan.retail;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.infinispan.retail.model.RetailProduct;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RetailCatalogResourceTest {

    @Test
    public void getExistingProduct() {
        RetailProduct domesticProduct = given().when().get("/products/{code}", "c123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(RetailProduct.class);
        assertThat(domesticProduct.code).isEqualTo("c123");

        given().when().get("/products/{code}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getProductNotFound() {
        given().when().get("/products/{code}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testList() {
        RetailProduct[] inventory = given().when()
              .get("/products")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("$.size()", is(1))
              .extract().as(RetailProduct[].class);
        assertThat(inventory).hasSize(1);
    }

    @Test
    public void testCRUD() {
        given()
              .body("{\"code\": \"pantsCode\", \"name\": \"pants\"}")
              .contentType(ContentType.JSON)
              .when()
              .post("/products")
              .then()
              .statusCode(Response.Status.CREATED.getStatusCode());

        RetailProduct createdProduct = given().when().get("/products/{code}", "pantsCode")
              .then()
              .statusCode(Response.Status.OK.getStatusCode()).extract()
              .body().as(RetailProduct.class);
        assertThat(createdProduct.code).isEqualTo("pantsCode");
        assertThat(createdProduct.name).isEqualTo("pants");

        given()
              .body("{\"name\": \"amazing pants\" }")
              .contentType(ContentType.JSON)
              .when()
              .put("/products/{code}", "pantsCode")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

        RetailProduct updatedProduct = given().when().get("/products/{code}", "pantsCode")
              .then()
              .statusCode(Response.Status.OK.getStatusCode()).extract()
              .body().as(RetailProduct.class);

        assertThat(updatedProduct.code).isEqualTo("pantsCode");
        assertThat(updatedProduct.name).isEqualTo("amazing pants");

        given().when().delete("/products/{code}", "pantsCode")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

        given().when().get("/products/{code}", "pantsCode")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testUpgradeStock() {
        RetailProduct retailProductPre = given().when().get("/products/{code}", "c123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(RetailProduct.class);

        assertThat(retailProductPre).isNotNull();

        given()
              .when().queryParam("stock", 3)
              .patch("/products/{code}", "c123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

        RetailProduct retailProductAfter = given().when().get("/products/{code}", "c123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(RetailProduct.class);

        assertThat(retailProductAfter.stock).isEqualTo(retailProductPre.stock + 3);
    }

    @Test
    public void testUpdateNotFound() {
        given().when().get("/products/{code}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}
