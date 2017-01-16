package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
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
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class IndexCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DaoFactory daoFactory = new MySqlDaoFactory();
        Connection connection;
        List<Question> lastQuestions = null;
        List<Question> mostRatedQuestions = null;
        try {
            connection = (Connection) daoFactory.getContext();
            MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            lastQuestions = questionDao.getLastQuestions(20);
            mostRatedQuestions = questionDao.getMostRatedQuestions(10);

        } catch (PersistException e) {
            e.printStackTrace();
        }

        req.setAttribute("lastQuestions", lastQuestions);
        req.setAttribute("mostRatedQuestions", mostRatedQuestions);
        req.getRequestDispatcher(CommandPath.INDEX).forward(req, resp);
    }
}
