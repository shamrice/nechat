package io.github.shamrice.nechat.core.configuration;

import io.github.shamrice.nechat.core.configuration.Db.DbConfiguration;
import io.github.shamrice.nechat.core.configuration.Db.DbCredentials;
import io.github.shamrice.nechat.core.configuration.Definitions.ConfigurationFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

        File dbConfigFile = new File(ConfigurationFiles.DB_CONFIGURATION_FILE_LOCATION);
        String dbConfigPath = dbConfigFile.getPath();


        if (!dbConfigFile.exists() && !dbConfigFile.isDirectory()) {
            System.out.println("Failed config location: " + dbConfigPath);
            System.out.println("Cannot find default db configuration location. Trying ./conf/db-config.properties");
            dbConfigPath = "conf/db-config.properties";
        }

        try {
            InputStream configInput = new FileInputStream(dbConfigPath);
            dbConfigProperties.load(configInput);
            configInput.close();
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
            System.out.println("Failed to find db config... exiting.");
            //System.exit(-1);
        }

        String instance = dbConfigProperties.getProperty("db.instance");
        String schema = dbConfigProperties.getProperty("db.schema");
        String username = dbConfigProperties.getProperty("db.username");
        String password = dbConfigProperties.getProperty("db.password");

        DbCredentials dbCredentials = new DbCredentials(instance, schema, username, password);

        return new DbConfiguration(dbCredentials);

    }
}
