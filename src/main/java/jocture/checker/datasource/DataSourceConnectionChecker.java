package jocture.checker.datasource;

import jocture.checker.datasource.factory.DataSourceFactory;
import jocture.checker.datasource.factory.DataSourceFactorySelecter;
import jocture.checker.datasource.properties.ConnectionProperties;
import jocture.checker.datasource.properties.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceConnectionChecker implements ExitCodeGenerator {

    private final DataSourceProperties dataSourceProperties;
    private final DataSourceFactorySelecter dataSourceFactorySelecter;
    private DataSourceFactory dataSourceFactory;
    private int exitCode= 0;

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkConnections(){
        selectDataSourceFactory();
        List<ConnectionProperties> propList = dataSourceProperties.getList();

        long successCount = propList.stream()
            .filter(this::checkConnection)
            .count();

        setExitCodeToErrorIfNotAllSuccess(successCount);
        log.info("Connection Check End : {}/{} (success/total)", successCount,propList.size());
    }

    private void selectDataSourceFactory() {
        dataSourceFactory = dataSourceFactorySelecter.select(dataSourceProperties.getType());
        log.debug("DatasourceFactory : {}", dataSourceFactory.getClass().getSimpleName());
    }

    private boolean checkConnection(ConnectionProperties prop) {
        log.debug("conn ={}",prop);
       if(!prop.validate()){
           return failBecauseOfInvalidProperty(prop);
       }
        return establishConnection(prop);
    }

    private boolean failBecauseOfInvalidProperty(ConnectionProperties prop) {
        log.error("FAIL : {} ({})", prop.getName(), prop.getUrl());
        return false;
    }

    private boolean establishConnection(ConnectionProperties prop) {
        // 예전 사용 방식 : 구성(configuration)과 사용이 분리되지 않는 형태로 좋지 않은 설계이다
        //Connection conn1 = DriverManager.getConnection(prop.getUrl(),prop.getUsername(), prop.getPassword());

        // 커넥션 생성
        // 커넥션 : 실제 물리 DB와 연결 세션(DataSource 를 통해 커넥션을 획득한다.
        // 커넥션 연결 방법 : DataSource룰 통해서 한다.

        // DataSource 인터페이스를 사용하면, 구성과 사용이 분리된다
        DataSource datasource = dataSourceFactory.create(prop);
        log.debug(">>>Datasource ={}",datasource);
        try (Connection conn = datasource.getConnection()) {
            log.info("SUCCESS : {} ({})", prop.getName(), prop.getUrl());
            return true;
        }catch (Exception e){
            log.error("FAIL : {} ({})", prop.getName(), prop.getUrl());
            return false;
        }
        
    }

    private void setExitCodeToErrorIfNotAllSuccess(long successCount) {
        if(successCount < dataSourceProperties.getList().size()){
            exitCode =1;
        }
    }
}



// 커넥션
//
// 결과값



// 스트림 : 메서드(연산자) 체인을 사용
// 연산자 종류
// 1) 종단 연산자 : forEach
// 2) 중간 연산자 : filter, map
// 종단 연산자로 끝나야지만 중간 연산자가 돌아간다


//forEach : 스트림 종단(Terminal) 연산자
// 중간(Intermediate) 연산자
