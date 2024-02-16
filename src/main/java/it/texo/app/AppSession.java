package it.texo.app;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import it.texo.app.movie.Movie;
import it.texo.app.movie.MovieConverter;
import it.texo.app.movie.MovieCsvBean;
import it.texo.app.utils.CsvUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AppSession {

    private static final Logger LOGGER = Logger.getLogger("AppSession");

    @Inject
    MovieConverter converter;

    void onStart(@Observes StartupEvent ev) throws Exception {
        LOGGER.info("The application is starting...");
        processCsv();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void processCsv() throws Exception {
        List<Movie> moviesEntities = new ArrayList<>();
        CsvUtil<MovieCsvBean> csvUtil = new CsvUtil<>();
        List<MovieCsvBean> movies = csvUtil.load(MovieCsvBean.class);
        movies.forEach(dto -> moviesEntities.add(converter.toEntity(dto)));
        Movie.persist(moviesEntities);
    }
}
