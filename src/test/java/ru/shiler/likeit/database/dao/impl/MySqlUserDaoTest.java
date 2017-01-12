package ru.shiler.likeit.database.dao.impl;

import org.junit.Test;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Evgeny Yushkevich on 02.12.2016.
 */
public class MySqlUserDaoTest {

    @Test
    public void testUserDao() throws PersistException, SQLException {
        MySqlDaoFactory daoFactory = new MySqlDaoFactory();
        Connection connection = daoFactory.getContext();
        MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(connection, User.class);
        connection.setAutoCommit(false);
        User user = userDao.getByPK(new Integer(1));
        System.out.println(user);
    }

}
