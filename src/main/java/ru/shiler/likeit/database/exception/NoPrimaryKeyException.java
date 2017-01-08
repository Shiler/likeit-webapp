package ru.shiler.likeit.database.exception;

/**
 * Created by Evgeny Yushkevich on 23.11.2016.
 */
public class NoPrimaryKeyException extends PersistException {

    public NoPrimaryKeyException() {
    }

    public NoPrimaryKeyException(String cause) {
        super(cause);
    }

    public NoPrimaryKeyException(Exception e) {
        super(e);
    }

    public NoPrimaryKeyException(String cause, Exception e) {
        super(cause, e);
    }

}
