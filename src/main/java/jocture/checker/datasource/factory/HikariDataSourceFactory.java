package jocture.checker.datasource.factory;

import com.zaxxer.hikari.HikariDataSource;
import jocture.checker.datasource.properties.ConnectionProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class HikariDataSourceFactory implements DataSourceFactory{

    @Override
    public boolean supports(Class<?> type) {
        return type == HikariDataSource.class;
    }

    public DataSource create(ConnectionProperties prop){

        // 커넥션 풀(Connection Pool; CP) : 커넥션을 미리 생성해 놀는 공간
        HikariDataSource dataSource =  new HikariDataSource();
        dataSource.setJdbcUrl(prop.getUrl());
        dataSource.setUsername(prop.getUsername());
        dataSource.setPassword(prop.getPassword());
        //dataSource.setPoolName("Pool-"+ prop.getName());
        return dataSource;
    }
}
