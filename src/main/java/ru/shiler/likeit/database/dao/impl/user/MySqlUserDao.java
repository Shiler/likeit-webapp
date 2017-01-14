package ru.shiler.likeit.database.dao.impl.user;

import ru.shiler.likeit.database.Utils;
import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.model.user.UserRole;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class MySqlUserDao extends AbstractJDBCDao<User, Integer> {

    private class PersistUser extends User {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public MySqlUserDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        addRelation(User.class, "userRole");
    }

    public List<User> getMostAnsweringUsers(int limit) throws PersistException {
        return getLimitOrderBy("answer_amount", limit, false);
    }

    public User getByUserName(String userName) throws PersistException {
        List<User> list;
        String sql = getSelectQuery();
        sql += " WHERE user_name = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, userName);
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
    public String getSelectQuery() {
        return "SELECT `id`, `user_name`, `full_name`, `email`, `password`, `create_time`, `user_role`, `status`, `age`, `hobbies`, `avatar`, `answer_amount` FROM `likeit`.`user` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`user` \n" +
                "(`user_name`, `full_name`, `email`, `password`, `create_time`, `user_role`, `status`, `age`, `hobbies`, `avatar`, `answer_amount`) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.user \n" +
                "SET `user_name` = ?, `full_name` = ?, `email` = ?, `password` = ?, `create_time` = ?, `user_role` = ?, `status` = ?, `age` = ?, `hobbies` = ?, `avatar` = ?, `answer_amount` \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit`.user WHERE id= ?;";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistException {
        List<User> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistUser user = new PersistUser();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setUserRole((UserRole) getDependence(UserRole.class, rs.getInt("user_role")));
                user.setStatus(rs.getInt("status"));
                user.setAge(rs.getInt("age"));
                user.setHobbies(rs.getString("hobbies"));
                user.setAvatar(rs.getInt("avatar"));
                user.setAnswerAmount(rs.getInt("answer_amount"));
                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistException {
        prepareStatement(statement, object);
    }

    protected void prepareStatement(PreparedStatement statement, User object) throws PersistException {
        try {
            int userRole = (object.getUserRole() == null || object.getUserRole().getId() == null) ? -1
                    : object.getUserRole().getId();
            statement.setString(1, object.getUserName());
            statement.setString(2, object.getFullName());
            statement.setString(3, object.getEmail());
            statement.setString(4, object.getPassword());
            statement.setTimestamp(5, object.getCreateTime());
            statement.setInt(6, userRole);
            statement.setInt(7, object.getStatus());
            statement.setInt(8, object.getAge());
            statement.setString(9, object.getHobbies());
            statement.setInt(10, object.getAvatar());
            statement.setInt(11, object.getAnswerAmount());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistException {
        prepareStatement(statement, object);
    }

}
