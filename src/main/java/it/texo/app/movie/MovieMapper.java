package it.texo.app.movie;

import it.texo.app.dto.MovieCsvDto;
import org.jboss.logmanager.Level;
import java.util.Objects;
import java.util.logging.Logger;

public class MovieMapper {
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(MovieMapper.class));

    public static Movie toEntity(MovieCsvDto dto) {
        Movie movie = new Movie();
        movie.title = dto.title;
        movie.producers = dto.producers;
        movie.studios = dto.studios;
        movie.winner = Objects.nonNull(dto.winner) && dto.winner.equals("yes");

        try {
            movie.year = Integer.valueOf(dto.year);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, "Year is not a number", e);
        }

        return movie;
    }
}
