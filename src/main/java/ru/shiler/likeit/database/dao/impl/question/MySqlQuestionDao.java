package ru.shiler.likeit.database.dao.impl.question;

import ru.shiler.likeit.database.Utils;
import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.question.QuestionType;
import ru.shiler.likeit.model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class MySqlQuestionDao extends AbstractJDBCDao<Question, Integer> {

    private Connection connection;

    private class PersistQuestion extends Question {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public MySqlQuestionDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        this.connection = connection;
        addRelation(Question.class, "questionType");
        addRelation(Question.class, "creator");
    }

    public List<Question> getByCategoryId(int catId) throws PersistException{
        List<Question> result = new ArrayList<>();
        String sql = getSelectQuery() + "WHERE `type` = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, catId);
            ResultSet rs = statement.executeQuery();
            result = parseResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Question> search(String query) throws PersistException {
        List<Question> result = new ArrayList<>();
        String sql = getSelectQuery() + "WHERE `title` LIKE \"%" + query.toLowerCase() + "%\";";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            result = parseResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int getAnswerAmount(int id) {
        String sql = "SELECT count(*) FROM answer where `question_id` = ?;";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Question> getLastQuestions(int amount) throws PersistException {
        return getLimitOrderBy("create_time", amount, false);
    }


    public List<Question> getMostRatedQuestions(int amount) throws PersistException {
        return getLimitOrderBy("rating", amount, false);
    }

    public boolean isUserRated(int userId, int questionId) throws PersistException {
        String sql = "SELECT `rated` from `user_rated_question` WHERE user_id = ? AND question_id = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, questionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `type`, `creator`, `status`, `title`, `content`, `create_time`, `update_time`, `rating` FROM `likeit`.`question` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`question` \n" +
                "(`type`, `creator`, `status`, `title`, `content`, `create_time`, `rating`) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.question \n" +
                "SET `type` = ?, `creator` = ?, `status` = ?, `title` = ?, `content` = ?, `create_time` = ?, `rating` = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit`.question WHERE id= ?;";
    }

    @Override
    protected List<Question> parseResultSet(ResultSet rs) throws PersistException {
        List<Question> result = new LinkedList<>();
        try {
            while (rs.next()) {
                PersistQuestion question = new PersistQuestion();
                question.setId(rs.getInt("id"));
                question.setQuestionType((QuestionType) getDependence(QuestionType.class, rs.getInt("type")));
                question.setCreator((User) getDependence(User.class, rs.getInt("creator")));
                question.setStatus(rs.getInt("status"));
                question.setTitle(rs.getString("title"));
                question.setContent(rs.getString("content"));
                question.setCreateTime(rs.getTimestamp("create_time"));
                question.setUpdateTime(rs.getTimestamp("update_time"));
                question.setRating(rs.getInt("rating"));
                result.add(question);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        for(Question q : result) {
            q.setAnswerAmount(getAnswerAmount(q.getId()));
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Question object) throws PersistException {
        try {
            int type = (object.getQuestionType() == null || object.getQuestionType().getId() == null) ? -1
                    : object.getQuestionType().getId();
            int creator = (object.getCreator() == null || object.getCreator().getId() == null) ? -1
                    : object.getCreator().getId();
            statement.setInt(1, type);
            statement.setInt(2, creator);
            statement.setInt(3, object.getStatus());
            statement.setString(4, object.getTitle());
            statement.setString(5, object.getContent());
            statement.setObject(6, object.getCreateTime());
            statement.setInt(7, object.getRating());
            statement.setInt(8, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Question object) throws PersistException {
        try {
            int type = (object.getQuestionType() == null || object.getQuestionType().getId() == null) ? -1
                    : object.getQuestionType().getId();
            int creator = (object.getCreator() == null || object.getCreator().getId() == null) ? -1
                    : object.getCreator().getId();
            statement.setInt(1, type);
            statement.setInt(2, creator);
            statement.setInt(3, object.getStatus());
            statement.setString(4, object.getTitle());
            statement.setString(5, object.getContent());
            statement.setObject(6, object.getCreateTime());
            statement.setInt(7, object.getRating());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

}
