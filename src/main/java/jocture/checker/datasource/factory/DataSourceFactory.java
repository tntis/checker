package jocture.checker.datasource.factory;

import jocture.checker.datasource.properties.ConnectionProperties;

import javax.sql.DataSource;

//OCP : 확장에는 열려잇고 ,수정에는 닫혀있다.
public interface DataSourceFactory {

    boolean supports(Class<?> clazz);

    DataSource create(ConnectionProperties prop);

    static DataSourceFactory getDefaultFactory(){
        return DefaultDataSourceFactory.getInstance();
    }

}

// 커넥션을 잡는게 무겁기 떄문에 커넥션 풀 방식을 사용 하는것이 좋음???