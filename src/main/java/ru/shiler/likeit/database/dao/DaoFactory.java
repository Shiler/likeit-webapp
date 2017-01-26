package ru.shiler.likeit.database.dao;

import ru.shiler.likeit.database.exception.PersistException;

/**
 * Defines a Factory interface for DAO objects
 * specified by <code>Class</code>
 *
 * @param <Context> factory context, e.g. <code>Connection</code>
 */
public interface DaoFactory<Context> {

    /**
     * Subsidiary interface for building DAO's
     *
     * @param <Context>
     */
    interface DaoCreator<Context> {
        GenericDao create(Context context);
    }

    /**
     * Context getter
     *
     * @return Context
     * @throws PersistException
     */
    Context getContext() throws PersistException;

    /**
     * @param context
     * @param dtoClass Corresponding model <code>Class</code>
     * @return DAO object for {@code dtoClass} model
     * @throws PersistException
     */
    GenericDao getDao(Context context, Class dtoClass) throws PersistException;

}
