package ru.shiler.likeit.command.impl.async;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Evgeny Yushkevich on 21.01.2017.
 */
public class LikeQuestionAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(LikeQuestionAsyncCommand.class);

    private DaoFactory daoFactory;
    private MySqlQuestionDao questionDao;
    private User user;
    private String questionId;
    private ResourceBundle bundle;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        questionId = request.getParameter("id");
        user = (User) request.getSession().getAttribute("USER");
        if (questionId == null || user == null) return;
        response.setCharacterEncoding("UTF-8");
        bundle = ResourceBundle.getBundle("app-strings");
        initDao();
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (!isLiked()) {
            like(resultMap);
        } else {
            dislike(resultMap);
        }
        response.getWriter().write(prepareJson(resultMap));
    }

    private String prepareJson(Map<String, String> resultMap) {
        return new Gson().toJson(resultMap);
    }

    private void like(Map<String, String> resultMap) {
        try {
            Question question = questionDao.getByPK(Integer.parseInt(questionId));
            if (user.getId() == question.getCreator().getId()) {
                resultMap.put("result", "false");
                return;
            }
            questionDao.like(user.getId(), Integer.parseInt(questionId));
            resultMap.put("result", "liked");
            question = questionDao.getByPK(Integer.parseInt(questionId));
            resultMap.put("newRating", String.valueOf(question.getRating()));
        } catch (PersistException e) {
            logger.error("Like error", e);
            resultMap.put("result", "false");
        }
    }

    private void dislike(Map<String, String> resultMap) {
        try {
            Question question = questionDao.getByPK(Integer.parseInt(questionId));
            questionDao.dislike(user.getId(), Integer.parseInt(questionId));
            resultMap.put("result", "disliked");
            if (user.getId() == question.getCreator().getId()) {
                resultMap.put("result", "false");
                return;
            }
            question = questionDao.getByPK(Integer.parseInt(questionId));
            resultMap.put("newRating", String.valueOf(question.getRating()));
        } catch (PersistException e) {
            logger.error("Like error", e);
            resultMap.put("result", "false");
        }
    }

    private void initDao() {
        daoFactory = new MySqlDaoFactory();
        try {
            questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    private boolean isLiked() {
        try {
            return questionDao.isUserRated(user.getId(), Integer.parseInt(questionId));
        } catch (PersistException e) {
            logger.error("Error in DAO queries", e);
            return false;
        }
    }

}
