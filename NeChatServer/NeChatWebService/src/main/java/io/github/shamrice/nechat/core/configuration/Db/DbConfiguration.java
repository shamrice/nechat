package io.github.shamrice.nechat.core.configuration.Db;

/**
 * Created by Erik on 10/19/2017.
 */
public class DbConfiguration {

    private DbCredentials dbCredentials;
    private boolean autoReconnect;

    public DbConfiguration(DbCredentials dbCredentials, boolean autoReconnect) {
        this.dbCredentials = dbCredentials;
        this.autoReconnect = autoReconnect;
    }

    public DbCredentials getDbCredentials() {
        return dbCredentials;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }
}
