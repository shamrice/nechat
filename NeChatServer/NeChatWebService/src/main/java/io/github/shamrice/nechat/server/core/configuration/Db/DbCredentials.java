package io.github.shamrice.nechat.server.core.configuration.Db;

/**
 * Created by Erik on 10/19/2017.
 */
public class DbCredentials {

    private String instance;
    private String schema;
    private String username;
    private String password;

    public DbCredentials(String instance, String schema, String username, String password) {
        this.instance = instance;
        this.schema = schema;
        this.username = username;
        this.password = password;
    }

    public String getInstance() {
        return instance;
    }

    public String getSchema() {
        return schema;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
