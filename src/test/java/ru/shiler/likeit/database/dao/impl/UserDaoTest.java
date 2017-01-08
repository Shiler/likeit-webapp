package ru.shiler.likeit.database.dao.impl;

import org.junit.Test;
import ru.shiler.likeit.database.dao.impl.mysql.DaoFactoryImpl;
import ru.shiler.likeit.database.dao.impl.mysql.UserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Evgeny Yushkevich on 02.12.2016.
 */
public class UserDaoTest {

    @Test
    public void testUserDao() throws PersistException, SQLException {
        DaoFactoryImpl daoFactory = new DaoFactoryImpl();
        Connection connection = daoFactory.getContext();
        UserDao userDao = (UserDao) daoFactory.getDao(connection, User.class);
        connection.setAutoCommit(false);
        User user = userDao.getByPK(new Integer(3));
        user.setName("dsadas");
        userDao.update(user);
        System.out.println(user);
    }

}
