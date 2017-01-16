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
 * Created by Evgeny Yushkevich on 15.01.2017.
 */
public class TopQuestionsCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DaoFactory daoFactory = new MySqlDaoFactory();
        Connection connection;
        List<Question> topQuestions = null;
        try {
            connection = (Connection) daoFactory.getContext();
            MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            topQuestions = questionDao.getMostRatedQuestions(20);

        } catch (PersistException e) {
            e.printStackTrace();
        }
        request.setAttribute("topQuestions", topQuestions);
        request.getRequestDispatcher(CommandPath.TOP).forward(request, response);
    }
}
