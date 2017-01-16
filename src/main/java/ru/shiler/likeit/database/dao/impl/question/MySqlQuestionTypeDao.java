package ru.shiler.likeit.database.dao.impl.question;


import ru.shiler.likeit.database.dao.AbstractJDBCDao;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.QuestionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class MySqlQuestionTypeDao extends AbstractJDBCDao<QuestionType, Integer> {

    private class PersistQuestionType extends QuestionType {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public MySqlQuestionTypeDao(DaoFactory<Connection> parentFactory, Connection connection) {
        super(parentFactory, connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT `id`, `name`, `name_ru` FROM `likeit`.`question_type` ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO `likeit`.`question_type` \n" +
                "(`name`, `name_ru`) \n" +
                "VALUES \n" +
                "(?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE `likeit`.`question_type` \n" +
                "SET `name` = ?, `name_ru` = ? \n" +
                "WHERE `id` = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM `likeit`.`question_type` WHERE `id` = ?";
    }

    @Override
    protected List<QuestionType> parseResultSet(ResultSet resultSet) throws PersistException {
        List<QuestionType> result = new LinkedList<>();
        try {
            while (resultSet.next()) {
                PersistQuestionType questionType = new PersistQuestionType();
                questionType.setId(resultSet.getInt("id"));
                questionType.setName(resultSet.getString("name"));
                questionType.setNameRu(resultSet.getString("name_ru"));
                result.add(questionType);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    private void prepareForInsertAndUpdate(PreparedStatement statement, QuestionType object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getNameRu());
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, QuestionType object) throws PersistException {
        prepareForInsertAndUpdate(statement, object);
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, QuestionType object) throws PersistException {
        prepareForInsertAndUpdate(statement, object);
    }

}
