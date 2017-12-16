package io.github.shamrice.nechat.server.logging;

import io.github.shamrice.nechat.server.core.configuration.Definitions.ConfigurationFiles;
import io.github.shamrice.nechat.server.logging.configuration.LoggerConfigurationStrings;
import io.github.shamrice.nechat.server.logging.loggers.ConsoleLoggerImpl;
import io.github.shamrice.nechat.server.logging.loggers.FileLoggerImpl;
import io.github.shamrice.nechat.server.logging.loggers.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Erik on 12/16/2017.
 */
public class Log {

    private static Log instance = null;
    private Logger logger = null;

    public static Logger get() {
        if (instance == null) {
            instance = new Log();
        }
        return instance.getLogger();
    }

    private Log() {
        Properties loggerProperties = new Properties();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(LoggerConfigurationStrings.LOGGER_CONIGURATION_FILE);
            loggerProperties.load(input);
            input.close();
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }

        String loggerType = loggerProperties.getProperty(LoggerConfigurationStrings.LOG_TYPE);
        String logLevel = loggerProperties.getProperty(LoggerConfigurationStrings.LOG_LEVEL);

        if (loggerType == null || loggerType.isEmpty()) {
            loggerType = LoggerConfigurationStrings.LOG_TYPE_CONSOLE;
        }

        LogLevel minLogLevel = LogLevel.DEBUG;

        if (logLevel != null && !logLevel.isEmpty()) {
            try {
                minLogLevel = LogLevel.valueOf(logLevel.toUpperCase());
            } catch (IllegalArgumentException illegalArgExc) {
                System.out.println("ERROR: Invalid log level set in config: " + logLevel + "\nERROR: Defaulting log level to DEBUG");
                illegalArgExc.printStackTrace();
            }
        } else {
            System.out.println("ERROR: Log level not set in configuration, setting to DEBUG.");
        }

        if (loggerType.toLowerCase().equals(LoggerConfigurationStrings.LOG_TYPE_FILE)) {
            String filelocation =  loggerProperties.getProperty(LoggerConfigurationStrings.LOG_FILE_LOCATION);

            if (filelocation != null && !filelocation.isEmpty()) {
                logger = new FileLoggerImpl(minLogLevel, filelocation);
            } else {
                logger = new FileLoggerImpl(minLogLevel);
            }

        } else if (loggerType.toLowerCase().equals(LoggerConfigurationStrings.LOG_TYPE_CONSOLE)) {
            logger = new ConsoleLoggerImpl(minLogLevel);
        } else {
            System.out.println("WARNING: LOGGER WAS UNABLE TO BE CONFIGURED. Log messages will not be shown!");
        }

    }

    public Logger getLogger() {
        if (logger != null) {
            return logger;
        } else {

            throw new NullPointerException("ERROR: Logger has not been set before attempting to be used!");
        }
    }


}
