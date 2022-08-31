package jocture.checker.datasource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceConnectionChecker implements ExitCodeGenerator {

    private final DataSourceProperties dataSourceProperties;
    private int exitCode= 0;

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkConnections(){
        List<ConnectionProperties> list = dataSourceProperties.getList();
        list.forEach(prop-> {
            log.debug("prop = {}",prop);
            // 커넥션 생성
            // 커넥션 : 실제 물리 DB와 연결 세션(DataSource 를 통해 커넥션을 획득한다.
            // 커넥션 연결 방법 : DataSource룰 통해서 한다.
            DataSource datasource = new DriverManagerDataSource(prop.getUrl(), prop.getUsername(), prop.getPassword());
            try (Connection conn = datasource.getConnection()) {
                log.info("SUCCESS : {} ({})", prop.getName(),prop.getUrl());
            }catch (Exception e){
                log.error("FAIL : {} ({})", prop.getName(),prop.getUrl());
                exitCode = 1;
            }

        });
    }

}
// 커넥션
//
// 결과값