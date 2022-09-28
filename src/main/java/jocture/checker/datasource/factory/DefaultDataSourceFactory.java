package jocture.checker.datasource.factory;

import jocture.checker.datasource.properties.ConnectionProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultDataSourceFactory implements DataSourceFactory {

    private static final DataSourceFactory INSTANCE = new DefaultDataSourceFactory();

    @Override
    public boolean supports(Class<?> type) {
        return true;
    } // 컴포넌트가 없기떄문에 로직을 타지 않음

    public DataSource create(ConnectionProperties prop){
        return new DriverManagerDataSource(prop.getUrl(), prop.getUsername(), prop.getPassword());
    }

    static DataSourceFactory getInstance(){
        return INSTANCE;
    }
}
