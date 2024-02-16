package it.texo.app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class MovieIntegrationTest {

    @Test
    public void is_min_interval_winner_validated() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("min[0].producer", equalTo("Joel Silver"))
                .body("min[0].interval", equalTo(1))
                .body("min[0].previousWin", equalTo(1990))
                .body("min[0].followingWin", equalTo(1991));
    }

    @Test
    public void is_max_interval_winner_validated() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("max[0].producer", equalTo("Matthew Vaughn"))
                .body("max[0].interval", equalTo(13))
                .body("max[0].previousWin", equalTo(2002))
                .body("max[0].followingWin", equalTo(2015));
    }

    @Test
    public void is_min_interval_winner_not_validated() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("min[0].producer", not("Christopher Nolan"))
                .body("min[0].interval", not(0))
                .body("min[0].previousWin", not(2024))
                .body("min[0].followingWin", not(2024));
    }

    @Test
    public void is_max_interval_winner_not_validated() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("max[0].producer", not("John Travolta"))
                .body("max[0].interval", not(50))
                .body("max[0].previousWin", not(1950))
                .body("max[0].followingWin", not(2000));
    }
}

