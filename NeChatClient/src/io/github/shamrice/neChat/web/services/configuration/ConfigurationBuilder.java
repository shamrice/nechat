package io.github.shamrice.neChat.web.services.configuration;

import io.github.shamrice.neChat.web.services.configuration.constants.ConfigurationConstants;
import io.github.shamrice.neChat.web.services.configuration.service.ServiceConfiguration;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.json.JSONObject;

import javax.naming.ConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
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
            System.out.println("Cannot find config file at default location: " + configPath +
                ". Trying: " + configPath);
            configPath = "resources/config.properties";
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

        return new ClientConfiguration(serviceConfiguration, buildUserCredentials());

    }

    private static UserCredentials buildUserCredentials() {
        String login = configProperties.getProperty(
                ConfigurationConstants.USER_PREFIX + ConfigurationConstants.USER_LOGIN);
        String password = configProperties.getProperty(
                ConfigurationConstants.USER_PREFIX + ConfigurationConstants.USER_PASSWORD);

        return new UserCredentials(login, password);
    }

    private static ServiceConfiguration buildServiceConfiguration() throws ConfigurationException {

        String serviceProtocol = null;
        String serviceHost = null;
        int servicePort = -1;
        String serviceWebApp = null;

        String locatorEnabled = configProperties.getProperty(
                ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_LOCATOR_ENABLED);

        //use locator service to build config
        if (locatorEnabled.toLowerCase().equals("true")) {
            System.out.println("Building web service configuration using locator service.");

            String locatorUrl = configProperties.getProperty(
                    ConfigurationConstants.WEBSERVICES_PREFIX +
                            ConfigurationConstants.WEBSERVICES_LOCATOR_URL);

            if (locatorUrl == null || locatorUrl.isEmpty()) {
                throw new ConfigurationException("Webservice locator url is not set in configuration.");
            }

            try {
                InputStream is = new URL(locatorUrl).openStream();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, Charset.forName("UTF-8"))
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                is.close();
                reader.close();

                JSONObject result = new JSONObject(sb.toString());

                System.out.println("Service locator received: " + result.toString());

                serviceProtocol = result.getString("protocol");
                serviceHost = result.getString("host");
                servicePort = result.getInt("port");
                serviceWebApp = result.getString("webapp");

                if (serviceHost == null || servicePort < 0 || serviceProtocol == null || serviceWebApp == null) {
                    throw new ConfigurationException("Unable to get service information from locator service " +
                            "at " + locatorUrl);
                }

            } catch (Exception ex) {
                System.out.println("Failed building web service configuration from locator service.");
                ex.printStackTrace();
            }
        } else {
            //use local config to build webservice config
            System.out.println("Building web service configuration using local config values.");

            serviceHost = configProperties.getProperty(
                    ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_HOST);

            String servicePortString = configProperties.getProperty(
                    ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_PORT);

            serviceProtocol = configProperties.getProperty(
                    ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_PROTOCOL,
                    "https");

            serviceWebApp = configProperties.getProperty(
                    ConfigurationConstants.WEBSERVICES_PREFIX + ConfigurationConstants.WEBSERVICES_WEBAPP,
                    "");

            if (serviceHost == null || servicePortString == null) {
                throw new ConfigurationException("webservice.host or webservice.port is null in configuration");
            }

            try {
                servicePort = Integer.parseInt(servicePortString);
            } catch (Throwable ex) {
                System.out.println("Unable to parse web service port from config: " + servicePort);
                ex.printStackTrace();
                throw ex;
            }
        }

        if (serviceHost != null && servicePort > 0 && serviceProtocol != null && serviceWebApp != null) {
            return new ServiceConfiguration(serviceHost, servicePort, serviceProtocol, serviceWebApp);
        } else {
            throw new ConfigurationException("Failed to build web service configuration");
        }
    }
}
