package io.github.shamrice.nechat.logging.loggers;

import io.github.shamrice.nechat.logging.LogLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Erik on 12/16/2017.
 */
public class ConsoleLoggerImpl implements Logger {

    LogLevel minLogLevel = LogLevel.DEBUG;

    public ConsoleLoggerImpl(LogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
    }

    @Override
    public void logMessage(LogLevel logLevel, String message) {
        if (logLevel.ordinal() >= this.minLogLevel.ordinal()) {
            System.out.println(
                    getFormattedString(logLevel, message, null)
            );
        }
    }

    @Override
    public void logExceptionWithMessage(String message, Exception exception) {
        System.out.println(
                getFormattedString(LogLevel.EXCEPTION, message, exception)
        );
    }

    @Override
    public void logException(Exception exception) {
        System.out.println(
                getFormattedString(LogLevel.EXCEPTION, exception.getMessage(), exception)
        );
    }

    private String getFormattedString(LogLevel logLevel, String message, Exception exception) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(df.format(new Date()));
        stringBuilder.append(" : ");
        stringBuilder.append(logLevel.name());
        stringBuilder.append(" : ");
        stringBuilder.append(message);
        if (exception != null) {
            stringBuilder.append("\n");
            stringBuilder.append(exception.toString());
        }

        return stringBuilder.toString();
    }
}
