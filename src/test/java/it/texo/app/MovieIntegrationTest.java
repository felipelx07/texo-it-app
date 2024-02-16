package it.texo.app;

import io.quarkus.test.junit.QuarkusTest;
import it.texo.app.movie.Movie;
import it.texo.app.movie.MovieConverter;
import it.texo.app.movie.MovieCsvBean;
import it.texo.app.movie.MovieResponse;
import it.texo.app.utils.CsvUtil;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class MovieIntegrationTest {

    @Inject
    MovieConverter converter;

    @Test //json structure testing
    public void is_structure_response_validated() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .extract()
                .body().as(MovieResponse.class);
    }

    @Test //performance testing //TODO will increase
    public void response_is_less_then_1200_milleseconds() {
        given()
                .when()
                .get("/movies")
                .then()
                .assertThat()
                .statusCode(200)
                .time(Matchers.lessThan(1200L));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void is_csv_loaded_and_save_on_db() {
        Movie.deleteAll(); //remove all movies on DB

        List<Movie> moviesEntities = new ArrayList<>();
        CsvUtil<MovieCsvBean> csvUtil = new CsvUtil<>();
        List<MovieCsvBean> movies = csvUtil.load(MovieCsvBean.class); //load csv file and parse to MovieDto
        movies.forEach(dto -> moviesEntities.add(converter.toEntity(dto)));

        var count = Movie.count();
        Assertions.assertEquals(0, count); //asert that don't have movies

        Movie.persist(moviesEntities); //save movies on DB
        var isNotEmpty = !given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .extract()
                .body().as(MovieResponse.class).getMax().isEmpty();

        Assertions.assertTrue(isNotEmpty); //assert is not empty
    }
}

