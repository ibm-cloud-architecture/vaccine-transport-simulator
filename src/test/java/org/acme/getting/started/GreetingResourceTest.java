package org.acme.getting.started;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import ibm.gse.eda.vaccine.transport.domain.TransportDefinition;
import ibm.gse.eda.vaccine.transport.domain.TransportationService;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {


    @Test
    public void testGetDef(){
       TransportationService service = new TransportationService();
       service.getAllTransportDefinitions().forEach(System.out::println);
        for (TransportDefinition item: service.getAllTransportDefinitions()) {
            System.out.println(item.from_loc);
        };
    }

    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
                .pathParam("name", uuid)
                .when().get("/transportation/greeting/{name}")
                .then()
                .statusCode(200)
                .body(is("hello " + uuid));
    }

}
