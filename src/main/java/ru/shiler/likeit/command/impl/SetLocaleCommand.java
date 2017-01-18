package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.command.CommandUtils;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.ConnectionPool;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class SetLocaleCommand implements Command {

    private final static Logger logger = Logger.getLogger(SetLocaleCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();

        if (lang != null) {
            session.setAttribute("locale", lang);
        }

        User user = (User) session.getAttribute("USER");
        if (user != null && !user.getLocale().equals(lang)) {
            Connection connection = ConnectionPool.getConnection();
            if (connection == null) {
                request.getRequestDispatcher(CommandPath.ERROR).forward(request, response);
                return;
            }
            user.setLocale(lang);
            DaoFactory daoFactory = new MySqlDaoFactory();
            try {
                MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(connection, User.class);
                userDao.update(user);
                connection.close();
            } catch (PersistException e) {
                logger.error("DAO not worked properly", e);
            } catch (SQLException e) {
                logger.warn("Unable to close connection", e);
            }
        }

        response.sendRedirect(CommandUtils.getBackUri(request));
    }

}
