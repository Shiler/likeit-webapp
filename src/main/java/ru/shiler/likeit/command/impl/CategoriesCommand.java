package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.ConnectionPool;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionTypeDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.question.QuestionType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 15.01.2017.
 */
public class CategoriesCommand implements Command {

    private final static Logger logger = Logger.getLogger(CategoriesCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        DaoFactory daoFactory = new MySqlDaoFactory();
        MySqlQuestionTypeDao questionTypeDao = null;
        MySqlQuestionDao questionDao = null;
        List<QuestionType> categories = new ArrayList<>();
        Connection connection = ConnectionPool.getConnection();

        if (connection == null) {
            request.getRequestDispatcher(CommandPath.ERROR).forward(request, response);
            return;
        }

        try {
            questionTypeDao = (MySqlQuestionTypeDao) daoFactory.getDao(connection, QuestionType.class);
            questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            categories = questionTypeDao.getAll();
        } catch (PersistException e) {
            logger.error("DAO unavailable", e);
        }

        List<Question> questions = new ArrayList<>();
        QuestionType category= new QuestionType();
        category.setName("All");
        category.setNameRu("Все");

        try {
            if (id != null && !id.equals("")) {
                questions = questionDao.getByCategoryId(Integer.valueOf(id));
                category = questionTypeDao.getByPK(Integer.valueOf(id));
            } else {
                questions = questionDao.getAll();
            }
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }

        request.setAttribute("category", category);
        request.setAttribute("questions", questions);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(CommandPath.CATEGORIES).forward(request, response);
    }
}
