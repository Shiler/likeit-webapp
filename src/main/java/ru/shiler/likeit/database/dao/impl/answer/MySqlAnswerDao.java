package ru.shiler.likeit.database.dao.impl.answer;

import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class MySqlAnswerDao extends AbstractJDBCDao<Answer, Integer> {

    private Connection connection;

    private class PersistAnswer extends Answer {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public MySqlAnswerDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        this.connection = connection;
        addRelation(Answer.class, "creator");
        addRelation(Answer.class, "question");
    }

    public void delete(int answerId) throws PersistException {
        String sql = "DELETE FROM `answer` WHERE `id` = ?;";
        String sql2 = "DELETE FROM `answer_rating` WHERE `answer_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, answerId);
            statement.executeUpdate();
            statement = getConnection().prepareStatement(sql2);
            statement.setInt(1, answerId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public double getAnswerRating(int answerId) throws PersistException {
        String sql = "SELECT AVG(`rate`) FROM `answer_rating` WHERE `answer_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, answerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public void rate(int answerId, int userId, int rate) throws PersistException {
        String sql = "SELECT `rate` FROM `answer_rating` WHERE `answer_id` = ? AND `user_id` = ?;";
        boolean isAlreadyRated;
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, answerId);
            statement.setInt(2, userId);
            isAlreadyRated = statement.executeQuery().next();
            if (isAlreadyRated) {
                sql = "UPDATE `answer_rating` SET `rate` = ? WHERE `answer_id` = ? AND `user_id` = ?;";
                statement = getConnection().prepareStatement(sql);
                statement.setInt(1, rate);
                statement.setInt(2, answerId);
                statement.setInt(3, userId);
                statement.executeUpdate();
            } else {
                sql = "INSERT INTO `answer_rating` (`answer_id`, `user_id`, `rate`) VALUES (?, ?, ?);";
                statement = getConnection().prepareStatement(sql);
                statement.setInt(1, answerId);
                statement.setInt(2, userId);
                statement.setInt(3, rate);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public int getVoteCount(int answerId) throws PersistException {
        String sql = "SELECT COUNT(`rate`) FROM `answer_rating` WHERE `answer_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, answerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public List<Answer> getByQuestionId(int questionId) throws PersistException {
        List<Answer> answers = new ArrayList<>();
        String sql = getSelectQuery() + "WHERE `question_id` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, questionId);
            ResultSet rs = statement.executeQuery();
            return parseResultSet(rs);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return answers;
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `user_id`, `question_id`, `create_time`, `text` FROM `likeit`.`answer` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`answer` \n" +
                "(`user_id`, `question_id`, `create_time`, `text`) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.answer \n" +
                "SET `user_id` = ?, `question_id` = ?, `create_time` = ?, `text` = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit`.answer WHERE id= ?;";
    }

    @Override
    protected List<Answer> parseResultSet(ResultSet rs) throws PersistException {
        List<Answer> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistAnswer answer = new PersistAnswer();
                answer.setId(rs.getInt("id"));
                answer.setCreator((User) getDependence(User.class, rs.getInt("user_id")));
                answer.setQuestion((Question) getDependence(Question.class, rs.getInt("question_id")));
                answer.setCreateTime(rs.getTimestamp("create_time"));
                answer.setText(rs.getString("text"));
                answer.setRating(getAnswerRating(answer.getId()));
                answer.setVoteCount(getVoteCount(answer.getId()));
                result.add(answer);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Answer object) throws PersistException {
        prepareStatement(statement, object);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Answer object) throws PersistException {
        prepareStatement(statement, object);
    }

    private void prepareStatement(PreparedStatement statement, Answer object) throws PersistException {
        try {
            int creator = (object.getCreator() == null || object.getCreator().getId() == null) ? -1
                    : object.getCreator().getId();
            int question = (object.getQuestion() == null || object.getQuestion().getId() == null) ? -1
                    : object.getQuestion().getId();
            statement.setInt(1, creator);
            statement.setInt(2, question);
            statement.setTimestamp(3, object.getCreateTime());
            statement.setString(4, object.getText());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }


}
