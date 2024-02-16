package it.texo.app;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/movies")
public class MovieController {

    @Inject
    MovieService service;

    @Inject
    MovieConverter converter;

    @GET
    public MovieResponse findWinnerMovies() {
        var movies = service.findWinnerMovies();
        return converter.toResponse(movies);
    }
}
