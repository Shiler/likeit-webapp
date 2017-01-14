package ru.shiler.likeit.database.dao.impl.answer;

import ru.shiler.likeit.database.Utils;
import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;
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

    public double getAnswerRating(int answerId) throws PersistException {
        String sql = "SELECT AVG(`rate`) FROM `answer_raiting` WHERE `answer_id` = ?;";
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
                answer.setCreateTime(rs.getDate("create_time"));
                answer.setText(rs.getString("text"));
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
            Date sqlDate = Utils.convert(object.getCreateTime());
            int creator = (object.getCreator() == null || object.getCreator().getId() == null) ? -1
                    : object.getCreator().getId();
            int question = (object.getQuestion() == null || object.getQuestion().getId() == null) ? -1
                    : object.getQuestion().getId();
            statement.setInt(1, creator);
            statement.setInt(2, question);
            statement.setDate(3, sqlDate);
            statement.setString(4, object.getText());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }


}
