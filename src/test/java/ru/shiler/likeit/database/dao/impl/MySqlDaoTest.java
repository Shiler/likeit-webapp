package ru.shiler.likeit.database.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized;
import ru.shiler.likeit.database.dao.GenericDao;
import ru.shiler.likeit.database.dao.Identified;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Evgeny Yushkevich on 23.11.2016.
 */

public class MySqlDaoTest extends GenericDaoTest<Connection> {

    public MySqlDaoTest(Class clazz, Identified<Integer> notPersistedDto) {
        super(clazz, notPersistedDto);
    }

    private Connection connection;

    private GenericDao dao;

    private static final MySqlDaoFactory factory = new MySqlDaoFactory();

    @Parameterized.Parameters
    public static Collection getParameters() {
        return Arrays.asList(new Object[][]{
                {User.class, new User()},
                {Question.class, new Question()}
        });
    }

    @Before
    public void setUp() throws PersistException, SQLException {
        connection = factory.getContext();
        connection.setAutoCommit(false);
        dao = factory.getDao(connection, daoClass);
    }

    @After
    public void tearDown() throws SQLException {
        context().rollback();
        context().close();
    }

    @Override
    public GenericDao dao() {
        return dao;
    }

    @Override
    public Connection context() {
        return connection;
    }

}

