package jocture.checker.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private List<ConnectionProperties> list;


}
