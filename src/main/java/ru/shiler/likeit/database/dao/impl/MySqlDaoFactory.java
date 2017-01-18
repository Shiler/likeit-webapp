package ru.shiler.likeit.database.dao.impl;

import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.GenericDao;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionTypeDao;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserRoleDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.question.QuestionType;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.model.user.UserRole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class MySqlDaoFactory implements DaoFactory<Connection> {

    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/likeit";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private Map<Class, DaoCreator> creators;

    @Deprecated
    public Connection getContext() throws PersistException {
        Connection connection;
        try {
            Properties info = new Properties();
            info.setProperty("user", user);
            info.setProperty("password", password);
            info.setProperty("useSSL", "true");
            info.setProperty("serverSslCert", "classpath:server.crt");
            info.setProperty("useLegacyDatetimeCode", "false");
            info.setProperty("serverTimezone", "Europe/Moscow");
            info.setProperty("useUnicode", "true");
            info.setProperty("characterEncoding", "utf8");
            connection = DriverManager.getConnection(url, info);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public MySqlDaoFactory() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creators = new HashMap<>();
        creators.put(User.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MySqlUserDao(MySqlDaoFactory.this, connection);
            }
        });
        creators.put(UserRole.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MySqlUserRoleDao(MySqlDaoFactory.this, connection);
            }
        });
        creators.put(Question.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MySqlQuestionDao(MySqlDaoFactory.this, connection);
            }
        });
        creators.put(QuestionType.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MySqlQuestionTypeDao(MySqlDaoFactory.this, connection);
            }
        });
        creators.put(Answer.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MySqlAnswerDao(MySqlDaoFactory.this, connection);
            }
        });
    }

}
