package it.texo.app.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MovieResponse {
    List<MovieResponseData> max;
    List<MovieResponseData> min;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class MovieResponseData {
    String producer;
    Integer interval;
    Integer previousWin;
    Integer followingWin;
}
