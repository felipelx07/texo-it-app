package it.texo.app;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import it.texo.app.dto.MovieCsvDto;
import it.texo.app.movie.Movie;
import it.texo.app.movie.MovieMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.jboss.logmanager.Level;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class AppSession {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        processCsv();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

    @Transactional
    void processCsv() {
        try {
            LOGGER.info("Starting import CSV Sheet...");
            URL path = Objects.requireNonNull(AppSession.class.getResource("/data/movielist.csv")).toURI().toURL();
            List<Movie> moviesEntities = new ArrayList<>();
            List<MovieCsvDto> movies = new CsvToBeanBuilder(new FileReader(path.getFile()))
                    .withType(MovieCsvDto.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
            movies.forEach(dto -> moviesEntities.add(MovieMapper.toEntity(dto)));
            Movie.persist(moviesEntities);
            LOGGER.info("CSV Sheet imported succesfuly!");
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error when try to load .CSV | message ", e);
        }
    }
}
