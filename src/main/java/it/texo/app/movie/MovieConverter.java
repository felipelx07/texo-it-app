package it.texo.app.movie;

import jakarta.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Dependent
public class MovieConverter {

    public Movie toEntity(MovieCsvBean dto) throws NumberFormatException {
        Movie movie = new Movie();
        movie.title = dto.title;
        movie.producers = dto.producers;
        movie.studios = dto.studios;
        movie.winner = Objects.nonNull(dto.winner) && dto.winner.equals("yes");
        movie.year = Integer.valueOf(dto.year);
        return movie;
    }

    public MovieResponse toResponse(List<Movie> winnerMovies) {
        AtomicInteger MIN_INTERVAL = new AtomicInteger(1);
        AtomicInteger MAX_INTERVAL = new AtomicInteger();
        MovieResponse movieResponse = new MovieResponse();
        List<MovieResponseData> groupAllMovieResponse = new ArrayList<>();
        Map<String, List<Movie>> moviesGroupedByProducer = groupByProducer(winnerMovies);
        moviesGroupedByProducer.forEach((producer, movies) -> {
            if (producer.isEmpty()) {
                return;
            }

            Movie lastWinnerMovie = null;
            for (Movie movie : movies) {
                int interval = lastWinnerMovie == null ? 0 : movie.year - lastWinnerMovie.year;

                if (interval > 0) {
                    if (interval > MAX_INTERVAL.get())
                        MAX_INTERVAL.set(interval);

                    groupAllMovieResponse.add(createMovieResponseData(producer, interval, lastWinnerMovie.year, movie.year));
                }

                lastWinnerMovie = movie;
            }
        });
        var sortedGroupAllMovieResponse = groupAllMovieResponse.stream().sorted(Comparator.comparing(MovieResponseData::getInterval));
        sortedGroupAllMovieResponse.forEach(responseData -> {
            if (responseData.interval <= MIN_INTERVAL.get())
                movieResponse.min.add(responseData);
            else if (responseData.interval == MAX_INTERVAL.get())
                movieResponse.max.add(responseData);
        });

        movieResponse.min.sort(Comparator.comparing(MovieResponseData::getPreviousWin));
        movieResponse.max.sort(Comparator.comparing(MovieResponseData::getPreviousWin));

        return movieResponse;
    }

    private static Map<String, List<Movie>> groupByProducer(List<Movie> movies) {
        Map<String, List<Movie>> groupByProducerResult = new HashMap<>();
        movies.forEach(movie -> cleanProducerNames(movie.producers)
                .forEach(producer -> groupByProducerResult.computeIfAbsent(producer, k -> new ArrayList<>())
                        .add(movie)));
        return groupByProducerResult;
    }

    private static List<String> cleanProducerNames(String producers) {
        return Arrays.stream(producers.split(" and |,")).map(String::trim).collect(Collectors.toList());
    }

    private static MovieResponseData createMovieResponseData(String producer, int interval, int previousYear, int followingYear) {
        return new MovieResponseData(producer, interval, previousYear, followingYear);
    }
}
