package ru.shiler.likeit.database.dao.impl.mysql;

import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class UserDao extends AbstractJDBCDao<User, Integer> {

    private class PersistUser extends User {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public UserDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `username`, `email`, `password`, `create_time`, `update_time`, `role`, `status`, `age`, `hobbies`, `avatar`, `name` FROM `likeit!`.`user` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit!`.`user` \n" +
                "(`username`, `email`, `password`, `role`, `status`, `name`) \n" +
                "VALUES \n" +
                "(?, ?, md5(?), ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit!`.user \n" +
                "SET username = ?, email  = ?, password = md5(?), role = ?, status = ?, age = ?, hobbies = ?, avatar = ?, name = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit!`.user WHERE id= ?;";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistException {
        List<User> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistUser user = new PersistUser();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCreateTime(rs.getString("create_time"));
                user.setUpdateTime(rs.getString("update_time"));
                user.setRole(rs.getInt("role"));
                user.setStatus(rs.getInt("status"));
                user.setAge(rs.getInt("age"));
                user.setHobbies(rs.getString("hobbies"));
                user.setAvatar(rs.getInt("avatar"));
                user.setName(rs.getString("name"));
                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistException {
        try {
            String str = "`username`, `email`, `password`, `create_time`, `update_time`, `role`, `status`, `age`, `hobbies`, `avatar`, `name`";
            statement.setString(1, object.getUsername());
            statement.setString(2, object.getEmail());
            statement.setString(3, object.getPassword());
            statement.setInt(4, object.getRole());
            statement.setInt(5, object.getStatus());
            statement.setInt(6, object.getAge());
            statement.setString(7, object.getHobbies());
            statement.setInt(8, object.getAvatar());
            statement.setString(9, object.getName());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistException {
        try {
            statement.setString(1, object.getUsername());
            statement.setString(2, object.getEmail());
            statement.setString(3, object.getPassword());
            statement.setInt(4, object.getRole());
            statement.setInt(5, object.getStatus());
            statement.setString(6, object.getName());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
