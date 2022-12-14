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
        // ?????? ?????? ?????? : ??????(configuration)??? ????????? ???????????? ?????? ????????? ?????? ?????? ????????????
        //Connection conn1 = DriverManager.getConnection(prop.getUrl(),prop.getUsername(), prop.getPassword());

        // ????????? ??????
        // ????????? : ?????? ?????? DB??? ?????? ??????(DataSource ??? ?????? ???????????? ????????????.
        // ????????? ?????? ?????? : DataSource??? ????????? ??????.

        // DataSource ?????????????????? ????????????, ????????? ????????? ????????????
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



// ?????????
//
// ?????????



// ????????? : ?????????(?????????) ????????? ??????
// ????????? ??????
// 1) ?????? ????????? : forEach
// 2) ?????? ????????? : filter, map
// ?????? ???????????? ??????????????? ?????? ???????????? ????????????


//forEach : ????????? ??????(Terminal) ?????????
// ??????(Intermediate) ?????????
