package io.github.shamrice.nechat.logging.loggers;

import io.github.shamrice.nechat.logging.LogLevel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Erik on 12/16/2017.
 */
public class FileLoggerImpl implements Logger {

    private LogLevel minLogLevel;
    private String logFileLocation = "";

    public FileLoggerImpl(LogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
    }

    public FileLoggerImpl(LogLevel minLogLevel, String logFileLocation) {
        this.minLogLevel = minLogLevel;
        if (logFileLocation != null && !logFileLocation.isEmpty()) {
            if (!logFileLocation.endsWith("/")) {
                logFileLocation += "/";
            }

            //create new directory if it doesn't already exist.
            if (!new File(logFileLocation).mkdirs()) {
                    if (!new File(logFileLocation).exists()) {
                        System.out.println("ERROR: Attempted to make logging directory but failed. " +
                        "Log messages will not be recorded.");
                    }
            }

            this.logFileLocation = logFileLocation;
        }
    }

    @Override
    public void logMessage(LogLevel logLevel, String message) {
        if (logLevel.ordinal() >= minLogLevel.ordinal()) {
            writeToFile(
                    getFormattedString(logLevel, message, null)
            );
        }
    }

    @Override
    public void logExceptionWithMessage(String message, Exception exception) {
        writeToFile(
                getFormattedString(LogLevel.EXCEPTION, message, exception)
        );
    }

    @Override
    public void logException(Exception exception) {
        writeToFile(
                getFormattedString(LogLevel.EXCEPTION, exception.getMessage(), exception)
        );
    }

    private void writeToFile(String stringToWrite) {
        BufferedWriter writer = null;

        //new log every day
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String logDate = df.format(new Date());

        String fileName = logFileLocation + "NeChat_" + logDate + ".log";

        try {
            File logFile = new File(fileName);
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(stringToWrite);
        } catch (IOException ioExc) {
            System.out.println("Unable to write to log file: " + fileName);
            ioExc.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private String getFormattedString(LogLevel logLevel, String message, Exception exception) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String logLevelDesignator = logLevel.name().substring(0, 1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(df.format(new Date()));
        stringBuilder.append(" : ");
        stringBuilder.append(logLevelDesignator);
        stringBuilder.append(" : ");
        stringBuilder.append(message);
        if (exception != null) {
            stringBuilder.append("\n");
            stringBuilder.append(exception.toString());
        }
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
