package it.texo.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MovieIntegrationTest {

    @Test
    public void test_max_interval_empty() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("max", is(Collections.emptyList()));
    }

    @Test
    public void test_first_min_interval() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("min[0].producer", equalTo("Joel Silver"))
                .body("min[0].interval", equalTo(1))
                .body("min[0].previousWin", equalTo(1990))
                .body("min[0].followingWin", equalTo(1991));
    }
}

