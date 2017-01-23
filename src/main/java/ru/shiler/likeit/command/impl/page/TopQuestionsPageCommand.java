package ru.shiler.likeit.command.impl.page;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.SimpleCommand;
import ru.shiler.likeit.constants.JspPath;
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
 * Created by Evgeny Yushkevich on 15.01.2017.
 */
public class TopQuestionsPageCommand implements SimpleCommand {

    private final static Logger logger = Logger.getLogger(TopQuestionsPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = ConnectionPool.getConnection();
        if (connection == null) {

        }
        DaoFactory daoFactory = new MySqlDaoFactory();

        List<Question> topQuestions = null;
        try {
            connection = (Connection) daoFactory.getContext();
            if (connection == null) {
                request.getRequestDispatcher(JspPath.ERROR).forward(request, response);
                return;
            }
            MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            topQuestions = questionDao.getMostRatedQuestions(20);
            connection.close();
        } catch (PersistException e) {
            logger.error("DAO not worked properly", e);
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }
        request.setAttribute("topQuestions", topQuestions);
        request.getRequestDispatcher(JspPath.TOP).forward(request, response);
    }
}
