package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class SearchCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        if (query != null) {
            DaoFactory daoFactory = new MySqlDaoFactory();
            List<Question> questions;
            try {
                MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(daoFactory.getContext(), Question.class);
                questions = questionDao.search(query);
                req.setAttribute("searchResult", questions);
                req.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(req, resp);
            } catch (PersistException e) {
                e.printStackTrace();
            }
        } else {
            resp.sendRedirect("/index");
        }
    }

}
