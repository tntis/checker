package jocture.checker.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Setter @Getter
@ToString
public class ConnectionProperties {
    private String name;
    private String url;
    private String username;
    private String password;

    boolean validate(){
        return StringUtils.hasText(url) && StringUtils.hasText(username);
    }
}




// 1급 컬렉션
