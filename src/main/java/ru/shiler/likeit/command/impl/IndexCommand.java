package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.ConnectionPool;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class IndexCommand implements Command {

    private final static Logger logger = Logger.getLogger(CategoriesCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = ConnectionPool.getConnection();
        if (connection == null) {
            req.getRequestDispatcher(CommandPath.ERROR).forward(req, resp);
            return;
        }
        DaoFactory daoFactory = new MySqlDaoFactory();
        List<Question> lastQuestions = null;
        List<Question> mostRatedQuestions = null;
        List<User> topUsers = null;
        try {
            MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(connection, User.class);
            lastQuestions = questionDao.getLastQuestions(20);
            mostRatedQuestions = questionDao.getMostRatedQuestions(10);
            topUsers = userDao.getTopUsers(5);
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }

        req.setAttribute("lastQuestions", lastQuestions);
        req.setAttribute("mostRatedQuestions", mostRatedQuestions);
        req.setAttribute("topUsers", topUsers);
        req.getRequestDispatcher(CommandPath.INDEX).forward(req, resp);
    }
}
