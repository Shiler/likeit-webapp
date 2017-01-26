package ru.shiler.likeit.database.dao;

import ru.shiler.likeit.database.exception.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Specifies basic DAO behaviour.
 *
 * @param <T>  model class
 * @param <PK> primary key
 */
public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    /**
     * Persists DAO model's object to the database
     *
     * @param object model object
     * @return persisted object if no exception
     * @throws PersistException
     */
    T persist(T object) throws PersistException;

    /**
     * Returns object composed of its database specified columns if it exists.
     *
     * @param key primary key of the model
     * @return model object with specify primary key
     * @throws PersistException
     */
    T getByPK(PK key) throws PersistException;

    /**
     * Updates model's object fields in the database if it exists.
     *
     * @param object model object
     * @throws PersistException
     */
    void update(T object) throws PersistException;

    /**
     * Deletes database entries connected with this model.
     *
     * @param object to be deleted
     * @throws PersistException
     */
    void delete(T object) throws PersistException;

    /**
     * @return {@link List} of all items with this type.
     * @throws PersistException
     */
    List<T> getAll() throws PersistException;

}
