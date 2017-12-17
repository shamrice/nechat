package io.github.shamrice.nechat.logging.loggers;

import io.github.shamrice.nechat.logging.LogLevel;

/**
 * Created by Erik on 12/16/2017.
 */
public interface Logger {

    void logMessage(LogLevel logLevel, String message);
    void logExceptionWithMessage(String message, Exception exception);
    void logException(Exception exception);


}
