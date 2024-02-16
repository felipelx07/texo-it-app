package it.texo.app.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class CsvUtil<T> {
    public List<T> load(Class<T> beanType) throws Exception {
        URL path = Objects.requireNonNull(CsvUtil.class.getResource("/data/movielist.csv")).toURI().toURL();
        return new CsvToBeanBuilder<T>(new FileReader(path.getFile()))
                .withType(beanType)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSeparator(';')
                .withSkipLines(1)
                .build()
                .parse();
    }
}