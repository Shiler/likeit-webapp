package ru.shiler.likeit.command.impl.page;

import com.google.gson.Gson;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeny Yushkevich on 23.01.2017.
 */
public class UserPageCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(UserPageCommand.class);

    private DaoFactory daoFactory;
    private MySqlUserDao userDao;
    private MySqlQuestionDao questionDao;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        if (idString == null) {
            response.sendRedirect("/index");
            return;
        }
        initDao();
        int userId = Integer.parseInt(idString);
        User user;
        List<Question> userQuestions;
        int questionCount;
        int answerCount;
        try {
            user = userDao.getByPK(userId);
            answerCount = userDao.getAnswerCount(userId);
            questionCount = userDao.getQuestionCount(userId);
            userQuestions = questionDao.searchByUser(userId);

        } catch (PersistException e) {
            logger.error("User getByPK error", e);
            response.sendRedirect("/index");
            return;
        }
        request.setAttribute("profile", user);
        request.setAttribute("questionCount", questionCount);
        request.setAttribute("answerCount", answerCount);
        request.setAttribute("userQuestions", userQuestions);
        request.getRequestDispatcher(JspPath.PROFILE).forward(request, response);
    }


    private void initDao() {
        daoFactory = new MySqlDaoFactory();
        try {
            userDao = (MySqlUserDao) daoFactory.getDao(getConnection(), User.class);
            questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

}
