package dev.mv.utils.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static LoggerOutput loggerOutput = null;
    private static OutputStream outputStream = null;

    public enum LogLevel{
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public interface LoggerOutput {
        void output(String logOutput, LogLevel logLevel);
    }

    public static void setLogOutput(LoggerOutput logOutput) {
        loggerOutput = logOutput;
    }

    public static void setLogOutput(OutputStream output) {
        outputStream = output;
    }

    public static void debug(String msg) {
        output(buildLog(msg, LogLevel.DEBUG), LogLevel.DEBUG);
    }

    public static void info(String msg) {
        output(buildLog(msg, LogLevel.INFO), LogLevel.INFO);
    }

    public static void warn(String msg) {
        output(buildLog(msg, LogLevel.WARN), LogLevel.WARN);
    }

    public static void error(String msg) {
        output(buildLog(msg, LogLevel.ERROR), LogLevel.ERROR);
    }

    private static String buildLog(String msg, LogLevel logLevel) {
        return "[" + new SimpleDateFormat("yyyy-MM-dd ' ' HH:mm:ss:SSS").format(new Date()) + "] <" + logLevel + "> " + msg;
    }

    private static void output(String log, LogLevel logLevel) {
        if(loggerOutput != null) loggerOutput.output(log, logLevel);
        if(outputStream != null) {
            try {
                outputStream.write(log.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
