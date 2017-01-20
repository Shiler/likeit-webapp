package ru.shiler.likeit.command.impl.page;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.constants.JspPath;
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
public class CategoriesPageCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(CategoriesPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        DaoFactory daoFactory = new MySqlDaoFactory();
        MySqlQuestionTypeDao questionTypeDao = null;
        MySqlQuestionDao questionDao = null;
        List<QuestionType> categories = new ArrayList<>();

        try {
            questionTypeDao = (MySqlQuestionTypeDao) daoFactory.getDao(getConnection(), QuestionType.class);
            questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
            categories = questionTypeDao.getAll();
        } catch (PersistException e) {
            logger.error("DAO error", e);
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


        request.setAttribute("category", category);
        request.setAttribute("questions", questions);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(JspPath.CATEGORIES).forward(request, response);
    }
}
