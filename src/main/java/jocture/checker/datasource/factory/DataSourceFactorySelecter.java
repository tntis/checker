package jocture.checker.datasource.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataSourceFactorySelecter {

    private final List<DataSourceFactory> dataSourceFactories;

    public DataSourceFactory select(Class<? extends DataSource> type){
        Optional<DataSourceFactory> factory = dataSourceFactories.stream()
                .filter(f -> f.supports(type))
                .findFirst();
        return factory.orElseGet(DataSourceFactory::getDefaultFactory);
    }
}
