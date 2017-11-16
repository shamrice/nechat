package io.github.shamrice.neChat.web.services.configuration;

import io.github.shamrice.neChat.web.services.configuration.constants.ConfigurationConstants;
import io.github.shamrice.neChat.web.services.configuration.service.ServiceConfiguration;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Erik on 11/12/2017.
 */
public class ConfigurationBuilder {

    private static Properties configProperties = new Properties();

    public static ClientConfiguration build() {

        File configFile = new File(ConfigurationConstants.CONFIGURATION_FILE_LOCATION);
        String configPath = configFile.getPath();

        if (!configFile.exists() || configFile.isDirectory()) {
            configPath = "config.properties";
            System.out.println("Cannot find config file at default location: " + configPath +
                ". Trying: " + configPath);
        }

        try {
            InputStream configInputStream = new FileInputStream(configPath);
            configProperties.load(configInputStream);
            configInputStream.close();;
        } catch (IOException ioExc) {
            System.out.println("Unable to load configuration properties from config file");
            ioExc.printStackTrace();
            System.exit(-1);
        }

        ServiceConfiguration serviceConfiguration = null;

        try {
            serviceConfiguration = buildServiceConfiguration();
        } catch (ConfigurationException configEx) {
            System.out.println("Unable to build service configuration.");
            configEx.printStackTrace();
            System.exit(-2);
        }

        return new ClientConfiguration(serviceConfiguration);

    }

    private static ServiceConfiguration buildServiceConfiguration() throws ConfigurationException {

        String serviceHost = configProperties.getProperty(
                ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_HOST);

        String servicePort = configProperties.getProperty(
                ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_PORT);

        String serviceProtocol = configProperties.getProperty(
                ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_PROTOCOL,
                "https");

        if (serviceHost == null || servicePort == null) {
            throw new ConfigurationException("webservice.host or webservice.port is null in configuration");
        }

        int servicePortInt = 8080;

        try {
            servicePortInt = Integer.parseInt(servicePort);
        } catch (Throwable ex) {
            System.out.println("Unable to parse web service port from config: " + servicePort);
            ex.printStackTrace();
            throw ex;
        }

        return new ServiceConfiguration(serviceHost, servicePortInt, serviceProtocol);

    }
}
