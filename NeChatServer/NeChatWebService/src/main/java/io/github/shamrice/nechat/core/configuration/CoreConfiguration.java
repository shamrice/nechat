package io.github.shamrice.nechat.core.configuration;

import io.github.shamrice.nechat.core.configuration.Db.DbConfiguration;

/**
 * Created by Erik on 10/19/2017.
 */
public class CoreConfiguration {

    private DbConfiguration dbConfiguration;

    public CoreConfiguration(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    public DbConfiguration getDbConfiguration() {
        return dbConfiguration;
    }

}
