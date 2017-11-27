package io.github.shamrice.nechat.core.configuration;

import io.github.shamrice.nechat.core.configuration.Db.DbConfiguration;
import io.github.shamrice.nechat.core.configuration.Db.DbCredentials;
import io.github.shamrice.nechat.core.configuration.Definitions.ConfigurationFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Boolean.parseBoolean;

/**
 * Created by Erik on 10/19/2017.
 */
public class CoreConfigurationBuilder {

    public static CoreConfiguration build() {

        return new CoreConfiguration(
            buildDbConfiguration()
        );
    }

    private static DbConfiguration buildDbConfiguration() {

        Properties dbConfigProperties = new Properties();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(ConfigurationFiles.DB_CONFIGURATION_FILE);
            dbConfigProperties.load(input);
            input.close();
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }

        //TODO : use defined constant strings instead.
        String instance = dbConfigProperties.getProperty("db.instance");
        String schema = dbConfigProperties.getProperty("db.schema");
        String username = dbConfigProperties.getProperty("db.username");
        String password = dbConfigProperties.getProperty("db.password");
        String autoReconnectString = dbConfigProperties.getProperty("db.autoreconnect");

        boolean autoReconnect = Boolean.parseBoolean(autoReconnectString);

        DbCredentials dbCredentials = new DbCredentials(instance, schema, username, password);

        return new DbConfiguration(dbCredentials, autoReconnect);
    }
}
