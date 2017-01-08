package ru.shiler.likeit.database.dao.impl.mysql;

import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.User;
import ru.shiler.likeit.model.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 23.11.2016.
 */
public class UserRoleDao extends AbstractJDBCDao<UserRole, Integer> {

    private class PersistUserRole extends UserRole {
        public void setId(int id) {
            super.setId(id);
        }
    }


    public UserRoleDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `name` FROM `likeit!`.`user_role` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit!`.`user_role` \n" +
                "(`role_name`) \n" +
                "VALUES \n" +
                "(?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit!`.`user_role` \n" +
                "SET `role_name` = ? \n" +
                "WHERE `role_id` = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit!`.`user_role` WHERE `id` = ?";
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, UserRole object) throws PersistException {
        prepareForInsertAndUpdate(statement, object);
    }

    // insert and update are the same
    private void prepareForInsertAndUpdate(PreparedStatement statement, UserRole object) throws PersistException {
        try {
            statement.setString(1, object.getName());
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, UserRole object) throws PersistException {
        prepareForInsertAndUpdate(statement, object);
    }

    @Override
    protected List<UserRole> parseResultSet(ResultSet resultSet) throws PersistException {
        List<UserRole> result = new LinkedList<>();
        try {
            while (resultSet.next()) {
                PersistUserRole userRole = new PersistUserRole();
                userRole.setName(resultSet.getString("role_name"));
                result.add(userRole);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }
}
