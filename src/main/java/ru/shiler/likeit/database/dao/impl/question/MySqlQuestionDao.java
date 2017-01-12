package ru.shiler.likeit.database.dao.impl.question;

import ru.shiler.likeit.database.Utils;
import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.question.QuestionType;
import ru.shiler.likeit.model.user.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class MySqlQuestionDao extends AbstractJDBCDao<Question, Integer> {

    private class PersistQuestion extends Question {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public MySqlQuestionDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
        addRelation(Question.class, "questionType");
        addRelation(Question.class, "creator");
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `type`, `creator`, `status`, `title`, `content`, `create_time`, `update_time` FROM `likeit`.`question` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`question` \n" +
                "(`type`, `creator`, `status`, `title`, `content`, `create_time`) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.question \n" +
                "SET `type` = ?, `creator` = ?, `status` = ?, `title` = ?, `content` = ?, `create_time` = ? \n" +
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
                question.setCreateTime(rs.getDate("create_time"));
                question.setUpdateTime(rs.getTimestamp("update_time"));
                result.add(question);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    private void prepareStatement(PreparedStatement statement, Question object) throws PersistException {
        try {
            Date sqlDate = Utils.convert(object.getCreateTime());
            int type = (object.getQuestionType() == null || object.getQuestionType().getId() == null) ? -1
                    : object.getQuestionType().getId();
            int creator = (object.getCreator() == null || object.getCreator().getId() == null) ? -1
                    : object.getCreator().getId();
            statement.setInt(1, type);
            statement.setInt(2, creator);
            statement.setInt(3, object.getStatus());
            statement.setString(4, object.getTitle());
            statement.setString(5, object.getContent());
            statement.setDate(6, sqlDate);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Question object) throws PersistException {
        prepareStatement(statement, object);
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Question object) throws PersistException {
        prepareStatement(statement, object);
    }

}
