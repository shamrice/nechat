package io.github.shamrice.nechat.core.configuration.Db;

/**
 * Created by Erik on 10/19/2017.
 */
public class DbConfiguration {

    private DbCredentials dbCredentials;

    public DbConfiguration(DbCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }

    public DbCredentials getDbCredentials() {
        return dbCredentials;
    }
}
