package it.texo.app.movie;

import jakarta.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        MovieResponse movieResponse = new MovieResponse();
        List<MovieResponseData> groupAllMovieResponse = new ArrayList<>();
        Map<String, List<Movie>> moviesGroupedByProducer = groupByProducer(winnerMovies);
        moviesGroupedByProducer.forEach((producer, movies) -> {
            if (producer.isEmpty()) {
                return;
            }

            Optional<Movie> firstWinnerMovie = movies.stream().findFirst();
            Optional<Movie> lastWinnerMovie = movies.stream().skip(movies.size() - 1).findFirst();

            if (firstWinnerMovie.isPresent() && lastWinnerMovie.isPresent()) {
                int interval = lastWinnerMovie.get().year - firstWinnerMovie.get().year;
                if (interval > 0) {
                    groupAllMovieResponse.add(createMovieResponseData(producer, interval, firstWinnerMovie.get().year, lastWinnerMovie.get().year));
                }
            }
        });
        Supplier<Stream<MovieResponseData>> sortedGroupAllMovieResponse = () -> groupAllMovieResponse.stream().sorted(Comparator.comparing(MovieResponseData::getInterval));
        Optional<MovieResponseData> firstResponseData = sortedGroupAllMovieResponse.get().findFirst();
        Optional<MovieResponseData> lastResponseData = sortedGroupAllMovieResponse.get().skip(groupAllMovieResponse.size() - 1).findFirst();
        firstResponseData.ifPresent(movieResponseData -> movieResponse.min.add(movieResponseData));
        lastResponseData.ifPresent(movieResponseData -> movieResponse.max.add(movieResponseData));
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
