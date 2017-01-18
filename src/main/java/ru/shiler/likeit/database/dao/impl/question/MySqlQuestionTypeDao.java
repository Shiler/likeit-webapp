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

    public boolean isExists(String categoryName) throws PersistException {
        String sql = getSelectQuery() + "WHERE `name` = ? or `name_ru` = ?;";
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            statement.setString(1, categoryName);
            statement.setString(2, categoryName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    public QuestionType getByName(String categoryName) throws PersistException {
        String sql = getSelectQuery() + "WHERE `name` = ? or `name_ru` = ?;";
        PreparedStatement statement;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setString(1, categoryName);
            statement.setString(2, categoryName);
            ResultSet resultSet = statement.executeQuery();
            return parseResultSet(resultSet).get(0);
        } catch (SQLException e) {
            throw new PersistException();
        }
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

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, QuestionType object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getNameRu());
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, QuestionType object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getNameRu());
            statement.setInt(3, object.getId());
        } catch (SQLException e) {
            throw new PersistException();
        }
    }

}
