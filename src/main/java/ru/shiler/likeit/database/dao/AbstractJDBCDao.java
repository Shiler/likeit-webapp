package ru.shiler.likeit.database.dao;

import ru.shiler.likeit.database.exception.NoPrimaryKeyException;
import ru.shiler.likeit.database.exception.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */


/**
 * Abstract class extends default implementation of the CRUD operations using JDBC.
 *
 * @param <T>  object type
 * @param <PK> primary key type
 */
public abstract class AbstractJDBCDao<T extends Identified<PK>, PK extends Integer> implements GenericDao<T, PK> {

    private Connection connection;

    public AbstractJDBCDao(Connection connection) {
        this.connection = connection;
    }

    public abstract String getSelectQuery();
    public abstract String getCreateQuery();
    public abstract String getUpdateQuery();
    public abstract String getDeleteQuery();

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistException;
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    protected abstract List<T> parseResultSet(ResultSet resultSet) throws PersistException;

    @Override
    public T getByPK(PK key) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE `id` = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PersistException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void update(T object) throws PersistException {
        if (object.getId() == Integer.valueOf(0))  {
            throw new NoPrimaryKeyException();
        }
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object); // заполнение аргументов запроса оставим на совесть потомков
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistException {
        if (object.getId() == Integer.valueOf(0))  {
            throw new NoPrimaryKeyException();
        }
        String sql = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setObject(1, object.getId());
            } catch (Exception e) {
                throw new PersistException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
            statement.close();
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }


    @Override
    public T persist(T object) throws PersistException {
        T persistInstance;

        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistException("On persist modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }

        sql = getSelectQuery() + " WHERE `id` = last_insert_id();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new PersistException("Exception on find by PK new persist data.");
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return persistInstance;
    }

    @Override
    public List<T> getAll() throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return list;
    }
}
