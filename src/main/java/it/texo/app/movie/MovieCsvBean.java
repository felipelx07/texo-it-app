package it.texo.app.movie;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieCsvBean {

    @CsvBindByPosition(position = 0)
    public String year;

    @CsvBindByPosition(position = 1)
    public String title;

    @CsvBindByPosition(position = 2)
    public String studios;

    @CsvBindByPosition(position = 3)
    public String producers;

    @CsvBindByPosition(position = 4)
    public String winner;
}
