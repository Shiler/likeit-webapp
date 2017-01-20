package ru.shiler.likeit.database.dao.impl.user;

import ru.shiler.likeit.database.Utils;
import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.model.user.UserRole;

import java.sql.*;
import java.util.ArrayList;
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

    public int getTotalRating(int userId) throws PersistException {
        String sql = "SELECT SUM(`rate`) AS 'total' FROM  `answer_rating` INNER JOIN `answer` ON `answer_id` = `id` WHERE `answer`.`user_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    public List<User> getTopUsers(int limit) throws PersistException {
        List<User> userList = new ArrayList<>();
        int[] users = new int[limit];
        int[] rates = new int[limit];
        int size = limit;
        String sql = "SELECT a.`user_id`, SUM(r.`rate`) AS 'total_rating' FROM (`answer_rating` r INNER JOIN `answer` a ON `answer_id` = `id`) GROUP BY a.`user_id` ORDER BY `total_rating` DESC LIMIT ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            int index = 0;
            while(resultSet.next()) {
                users[index] = resultSet.getInt("user_id");
                rates[index] = resultSet.getInt("total_rating");
                index++;
            }
            if (index < limit) size = index;

            for (int i = 0; i < size; i++) {
                User user = getByPK(users[i]);
                user.setTotalRating(rates[i]);
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    public List<User> getMostAnsweringUsers(int limit) throws PersistException {
        return getLimitOrderBy("answer_amount", limit, false);
    }

    public boolean userHasQuestion(int userId, int questionId) throws PersistException {
        String sql = "SELECT * FROM `question` WHERE `creator` = ? AND `id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, questionId);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    public boolean userAnsweredQuestion(int userId, int questionId) throws PersistException {
        String sql = "SELECT * FROM `answer` WHERE `user_id` = ? AND `question_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, questionId);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            throw new PersistException();
        }
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

    public User getByEmail(String email) throws PersistException {
        List<User> list;
        String sql = getSelectQuery();
        sql += " WHERE email = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, email);
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
        return "SELECT `id`, `user_name`, `full_name`, `email`, `password`, `create_time`, `user_role`, `status`, `age`, `answer_amount`, `locale` FROM `likeit`.`user` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`user` \n" +
                "(`user_name`, `full_name`, `email`, `password`, `create_time`, `user_role`, `status`, `age`, `answer_amount`, `locale`) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.user \n" +
                "SET `user_name` = ?, `full_name` = ?, `email` = ?, `password` = ?, `create_time` = ?, `user_role` = ?, `status` = ?, `age` = ?, `answer_amount` = ?, `locale` = ? \n" +
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
                user.setAnswerAmount(rs.getInt("answer_amount"));
                user.setLocale(rs.getString("locale"));
                user.setTotalRating(getTotalRating(user.getId()));
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
            statement.setInt(9, object.getAnswerAmount());
            statement.setString(10, object.getLocale());
            statement.setInt(11, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistException {
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
            statement.setInt(9, object.getAnswerAmount());
            statement.setString(10, object.getLocale());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
