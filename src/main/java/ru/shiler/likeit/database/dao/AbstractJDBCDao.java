package ru.shiler.likeit.database.dao;


import ru.shiler.likeit.database.exception.PersistException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class provides basic implementation of the CRUD operations
 * using JDBC.
 *
 * @param <T>  persistence object type
 * @param <PK> primary key type
 */
public abstract class AbstractJDBCDao<T extends Identified<PK>,
        PK extends Integer> implements GenericDao<T, PK> {

    /**
     * Returns a sql query for getting all records.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     * Returns a sql query for a new record inserting to the database.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();

    /**
     * Returns a sql query for updating the records.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Returns a sql query for record deleting from the database.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * Decomposes {@link ResultSet} and returns <code>List</code> of objects
     * relevant to the <code>ResultSet</code>
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    /**
     * Sets up arguments of the <code>insert</code> query
     * in accordance with the values of the object fields
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object)
            throws PersistException;

    /**
     * Sets up arguments of the <code>update</code> query
     * in accordance with the values of the object fields
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistException;

    private DaoFactory<Connection> parentFactory;

    private Connection connection;

    private Set<ManyToOne> relations = new HashSet<ManyToOne>();

    @Override
    public T getByPK(Integer key) throws PersistException {
        List<T> list;
        String sql = getSelectQuery();
        sql += " WHERE id = ?";
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

    public List<T> getLimitOrderBy(String orderBy, int limit, boolean ascending) throws PersistException {
        List<T> result;
        String asc = ascending ? "ASC" : "DESC";
        String query = getSelectQuery() + "ORDER BY `" + orderBy + "` " + asc + " LIMIT ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            ResultSet rs = statement.executeQuery();
            result = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
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

    @Override
    public T persist(T object) throws PersistException {
        if (object.getId() != null && object.getId() != Integer.valueOf(0)) {
            throw new PersistException("Object is already persist.");
        }
        //saveDependences(object);

        T persistInstance;
        // Добавляем запись
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
        sql = getSelectQuery() + " WHERE id = last_insert_id();";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new PersistException("Exception on findByPK new persist data.");
            }
            persistInstance = list.iterator().next();
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return persistInstance;
    }

    @Override
    public void update(T object) throws PersistException {

        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
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
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public AbstractJDBCDao(DaoFactory<Connection> parentFactory, Connection connection) {
        this.parentFactory = parentFactory;
        this.connection = connection;
    }

    /**
     * Returns dependence of the DAO
     *
     * @param dtoClass model class
     * @param pk       primary key of the record
     * @return depending model object
     * @throws PersistException
     */
    protected Identified getDependence(Class<? extends Identified> dtoClass, Serializable pk) throws PersistException {
        return parentFactory.getDao(connection, dtoClass).getByPK(pk);
    }

    protected boolean addRelation(Class<? extends Identified> ownerClass, String field) {
        try {
            return relations.add(new ManyToOne(ownerClass, parentFactory, field));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveDependences(Identified owner) throws PersistException {
        for (ManyToOne m : relations) {
            try {
                if (m.getDependence(owner) == null) {
                    continue;
                }
                if (m.getDependence(owner).getId() == null) {
                    Identified depend = m.persistDependence(owner, connection);
                    m.setDependence(owner, depend);
                } else {
                    m.updateDependence(owner, connection);
                }
            } catch (Exception e) {
                throw new PersistException("Exception on save dependence in relation " + m + ".", e);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}