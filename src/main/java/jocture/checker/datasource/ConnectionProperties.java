package jocture.checker.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class ConnectionProperties {
    private String name;
    private String url;
    private String username;
    private String password;
}
