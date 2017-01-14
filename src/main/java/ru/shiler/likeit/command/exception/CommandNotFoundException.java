package ru.shiler.likeit.command.exception;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class CommandNotFoundException extends Exception {

    public CommandNotFoundException() {
        super();
    }

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CommandNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
