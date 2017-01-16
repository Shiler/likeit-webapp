package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 15.01.2017.
 */
public class CategoriesCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        DaoFactory daoFactory = new MySqlDaoFactory();
        MySqlQuestionTypeDao questionTypeDao = null;
        MySqlQuestionDao questionDao = null;
        List<QuestionType> categories = new ArrayList<>();
        try {
            questionTypeDao = (MySqlQuestionTypeDao) daoFactory.getDao(daoFactory.getContext(), QuestionType.class);
            questionDao = (MySqlQuestionDao) daoFactory.getDao(daoFactory.getContext(), Question.class);
            categories = questionTypeDao.getAll();
        } catch (PersistException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        request.setAttribute("category", category);
        request.setAttribute("questions", questions);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(CommandPath.CATEGORIES).forward(request, response);
    }
}
