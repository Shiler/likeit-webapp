package ru.shiler.likeit.database.dao.impl;

import org.junit.Test;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.model.user.UserRole;
import ru.shiler.likeit.security.Sha256;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

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

    @Test
    public void testCreateUser() throws PersistException, SQLException {
        MySqlDaoFactory daoFactory = new MySqlDaoFactory();
        Connection connection = daoFactory.getContext();
        MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(connection, User.class);
        connection.setAutoCommit(false);
        User user = new User();
        user.setEmail("aasd");
        user.setUserName("sddadadad");
        user.setFullName("sdadsa");
        user.setAge(Integer.parseInt("123"));
        user.setPassword(Sha256.encrypt("dsadadadada"));
        user.setLocale("en");
        UserRole userRole = new UserRole();
        userRole.setId(1);
        user.setUserRole(userRole);
        System.out.println(Sha256.encrypt("dsadadadada").length());
        user.setCreateTime(new Timestamp(new Date().getTime()));
        userDao.persist(user);
        System.out.println(userDao.getByUserName("sddadadad"));
        user.setLocale("ru");
        userDao.update(user);
    }

}
