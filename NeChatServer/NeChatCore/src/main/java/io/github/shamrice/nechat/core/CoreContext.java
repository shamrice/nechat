package io.github.shamrice.nechat.core;

import io.github.shamrice.nechat.core.configuration.CoreConfiguration;
import io.github.shamrice.nechat.core.configuration.CoreConfigurationBuilder;
import io.github.shamrice.nechat.core.configuration.Db.DbCredentials;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Erik on 10/19/2017.
 */
public class CoreContext {

    private static CoreContext instance = null;
    private CoreConfiguration coreConfiguration;
    private Connection connection;

    private CoreContext() {

        coreConfiguration = CoreConfigurationBuilder.build();

        if (connection == null) {
            try {
                DbCredentials dbCredentials = coreConfiguration.getDbConfiguration().getDbCredentials();

                System.out.println("Getting mysql jdbc driver");
                Class.forName("com.mysql.jdbc.Driver").newInstance();

                System.out.println("create sql connection to " + dbCredentials.getInstance());

                connection = DriverManager.getConnection(
                        "jdbc:mysql://" +
                                dbCredentials.getInstance() +
                                "/" +
                                dbCredentials.getSchema() +
                                "?user=" +
                                dbCredentials.getUsername() +
                                "&password=" +
                                dbCredentials.getPassword()
                );
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public static CoreContext getInstance() {
        if (instance == null) {
            instance = new CoreContext();
        }
        return instance;
    }

    public CoreConfiguration getCoreConfiguration() {
        return coreConfiguration;
    }

    public Connection getDatabaseConnection() {
        return connection;
    }
}
