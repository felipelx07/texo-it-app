package it.texo.app.movie;

import jakarta.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Dependent
public class MovieService {

    private static final int MAX_INTERVAL = 50;

    public MovieResponse findMinMaxInterval(List<Movie> winnerMovies) {
        MovieResponse movieResponse = new MovieResponse();
        List<MovieResponseData> minList = new ArrayList<>();
        List<MovieResponseData> maxList = new ArrayList<>();

        Map<String, List<Movie>> moviesGroupedByProducer = groupByProducer(winnerMovies);
        moviesGroupedByProducer.forEach((producer, movies) -> {
            if (producer.isEmpty()) {
                return;
            }

            Movie lastWinnerMovieOfProducer = null;
            for (Movie movie : movies) {
                if (Objects.isNull(lastWinnerMovieOfProducer))
                    lastWinnerMovieOfProducer = movie;

                int interval = movie.year - lastWinnerMovieOfProducer.year;

                if (interval > 0)
                    if (interval <= MAX_INTERVAL) {
                        minList.add(createMovieResponseData(producer, interval, lastWinnerMovieOfProducer.year, movie.year));
                    } else {
                        maxList.add(createMovieResponseData(producer, interval, lastWinnerMovieOfProducer.year, movie.year));
                    }
            }
        });

        movieResponse.max = maxList.stream().sorted(Comparator.comparing(MovieResponseData::getInterval)).collect(Collectors.toList());
        movieResponse.min = minList.stream().sorted(Comparator.comparing(MovieResponseData::getInterval)).collect(Collectors.toList());

        return movieResponse;
    }

    public Map<String, List<Movie>> groupByProducer(List<Movie> movies) {
        Map<String, List<Movie>> mapaProdutorFilmes = new HashMap<>();
        for (Movie movie : movies) {
            for (String producer : cleanProducerNames(movie.producers)) {
                mapaProdutorFilmes.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie);
            }
        }

        return mapaProdutorFilmes;
    }


    private List<String> cleanProducerNames(String producers) {
        return Arrays.stream(producers.split(" and |,")).map(String::trim).collect(Collectors.toList());
    }

    private MovieResponseData createMovieResponseData(String producer, int interval, int previousYear, int followingYear) {
        return new MovieResponseData(producer, interval, previousYear, followingYear);
    }
}
