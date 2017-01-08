package ru.shiler.likeit.database.dao;

import ru.shiler.likeit.database.exception.PersistException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    public T persist(T object) throws PersistException;

    public T getByPK(PK key) throws PersistException;

    public void update(T object) throws PersistException;

    public void delete(T object) throws PersistException;

    public List<T> getAll() throws PersistException;

}
