package ru.shiler.likeit.command.impl.page;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.constants.JspPath;
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
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class IndexPageCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(CategoriesPageCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DaoFactory daoFactory = new MySqlDaoFactory();
        List<Question> lastQuestions = null;
        List<Question> mostRatedQuestions = null;
        List<User> topUsers = null;
        try {
            MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
            MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(getConnection(), User.class);
            lastQuestions = questionDao.getLastQuestions(6);
            mostRatedQuestions = questionDao.getMostRatedQuestions(6);
            topUsers = userDao.getTopUsers(5);
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
        }

        req.setAttribute("lastQuestions", lastQuestions);
        req.setAttribute("mostRatedQuestions", mostRatedQuestions);
        req.setAttribute("topUsers", topUsers);
        req.getRequestDispatcher(JspPath.INDEX).forward(req, resp);
    }
}
