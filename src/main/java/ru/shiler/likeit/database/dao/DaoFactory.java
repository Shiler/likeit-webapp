package ru.shiler.likeit.database.dao;

import ru.shiler.likeit.database.exception.PersistException;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public interface DaoFactory<Context> {

    public interface DaoCreator<Context> {
        public GenericDao create(Context context);
    }

    public Context getContext() throws PersistException;

    public GenericDao getDao(Context context, Class dtoClass) throws PersistException;

}
