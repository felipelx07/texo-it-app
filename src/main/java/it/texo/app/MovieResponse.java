package it.texo.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MovieResponse {
    List<MovieResponseData> min = new ArrayList<>();
    List<MovieResponseData> max = new ArrayList<>();
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
