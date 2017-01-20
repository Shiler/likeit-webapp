package ru.shiler.likeit.command.exception;

/**
 * Created by Evgeny Yushkevich on 20.01.2017.
 */
public class NoDatabaseConnectionException extends Exception {

    public NoDatabaseConnectionException() {
        super();
    }

    public NoDatabaseConnectionException(String message) {
        super(message);
    }

    public NoDatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDatabaseConnectionException(Throwable cause) {
        super(cause);
    }

    protected NoDatabaseConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
