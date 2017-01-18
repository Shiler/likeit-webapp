package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.ConnectionPool;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;

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
public class SearchCommand implements Command {

    private final static Logger logger = Logger.getLogger(SearchCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        if (query != null) {
            DaoFactory daoFactory = new MySqlDaoFactory();
            List<Question> questions;
            try {
                Connection connection = ConnectionPool.getConnection();
                if (connection == null) {
                    req.getRequestDispatcher(CommandPath.ERROR).forward(req, resp);
                    return;
                }
                MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
                questions = questionDao.search(query);
                connection.close();
                req.setAttribute("searchResult", questions);
                req.getRequestDispatcher(CommandPath.SEARCH).forward(req, resp);
            } catch (PersistException e) {
                logger.error("DAO queries error", e);
            } catch (SQLException e) {
                logger.warn("Unable to close connection", e);
            }
        } else {
            resp.sendRedirect("/index");
        }
    }

}
