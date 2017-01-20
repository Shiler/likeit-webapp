package ru.shiler.likeit.command.impl.action;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.util.CommandUtils;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.security.Sha256;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class LoginCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(LoginCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().toLowerCase().equals("get")) {
            response.sendRedirect(CommandUtils.getBackUri(request));
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            response.sendRedirect("/login?error=empty_error");
            return;
        }

        User user = getUser(username, password, getConnection());

        if (user == null) {
            response.sendRedirect("/login?error=invalid_or_not_exists");
            return;
        } else {
            request.getSession().setAttribute("USER", user);

            response.sendRedirect("/index");
        }
    }

    private User getUser(String username, String password, Connection connection) {
        DaoFactory daoFactory = new MySqlDaoFactory();
        try {
            MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(connection, User.class);
            User user = userDao.getByUserName(username);
            User user2 = userDao.getByEmail(username);
            if (user == null && user2 == null) {
                return null;
            }
            if (user.getPassword().equals(Sha256.encrypt(password))) {
                return user;
            } else if (user2.getPassword().equals(Sha256.encrypt(password))) {
                return user2;
            } else {
                return null;
            }
        } catch (PersistException e) {
            logger.error("Can't get data from database", e);
        }
        return null;
    }
}
