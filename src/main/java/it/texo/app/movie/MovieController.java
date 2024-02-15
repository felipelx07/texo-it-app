package it.texo.app.movie;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/movies")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class MovieController {

    @Inject
    MovieService service;

    @GET
    public MovieResponse getProducerData() {
        List<Movie> movies = Movie.find("where winner is true order by year").list();
        return service.findMinMaxInterval(movies);
    }
}
