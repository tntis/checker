package jocture.checker.datasource.factory;

import jocture.checker.datasource.properties.ConnectionProperties;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbcpDataSourceFactory implements DataSourceFactory{

    @Override
    public boolean supports(Class<?> type) {
        return type == BasicDataSource.class;
    }

    public DataSource create(ConnectionProperties prop){

        // 커넥션 풀(Connection Pool; CP) : 커넥션을 미리 생성해 놀는 공간
        BasicDataSource dataSource =  new BasicDataSource();
        dataSource.setUrl(prop.getUrl());
        dataSource.setUsername(prop.getUsername());
        dataSource.setPassword(prop.getPassword());
        return dataSource;
    }
}
