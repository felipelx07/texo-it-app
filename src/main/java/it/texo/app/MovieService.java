package it.texo.app;

import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;
import java.util.List;

@Dependent
public class MovieService {

    @Transactional
    public List<Movie> findWinnerMovies() {
        return Movie.find("where winner is true order by year").list();
    }
}
