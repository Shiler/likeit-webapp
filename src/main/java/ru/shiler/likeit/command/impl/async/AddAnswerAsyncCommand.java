package ru.shiler.likeit.command.impl.async;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.util.TimestampUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Evgeny Yushkevich on 19.01.2017.
 */
public class AddAnswerAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(AddAnswerAsyncCommand.class);

    private ResourceBundle bundle;
    private DaoFactory daoFactory;
    private MySqlUserDao userDao;
    private MySqlQuestionDao questionDao;
    private MySqlAnswerDao answerDao;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initDAO();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String locale = (String) request.getSession().getAttribute("locale");
        bundle = ResourceBundle.getBundle("app-strings", new Locale(locale));
        User user = (User) request.getSession().getAttribute("USER");
        String text = request.getParameter("text");
        String questionId = request.getParameter("question_id");
        String responseJson = prepareJson(text, questionId, user, locale);
        response.getWriter().write(responseJson);
    }

    private void initDAO() {
        daoFactory = new MySqlDaoFactory();
        try {
            userDao = (MySqlUserDao) daoFactory.getDao(getConnection(), User.class);
            questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
            answerDao = (MySqlAnswerDao) daoFactory.getDao(getConnection(), Answer.class);
        } catch (PersistException e) {
            logger.error("Failed to create DAO", e);
        }
    }

    private String prepareJson(String text, String questionId, User user, String locale) {
        Map<String, String> resultMap = new LinkedHashMap<>();

        if (checkAll(user, text, questionId, resultMap)) {
            try {
                Answer answer = new Answer();
                answer.setCreator(user);
                answer.setText(text);
                answer.setCreateTime(new Timestamp(new Date().getTime()));
                answer.setQuestion(questionDao.getByPK(Integer.parseInt(questionId)));
                answerDao.persist(answer);
                resultMap.put("response", "success");
                resultMap.put("userId", String.valueOf(answer.getCreator().getId()));
                resultMap.put("fullName", answer.getCreator().getFullName());
                resultMap.put("text", answer.getText());
                resultMap.put("rating", bundle.getString("question.rating") + ": " + String.valueOf(answer.getRating()));
                resultMap.put("createTime", TimestampUtils.formatTimestamp(answer.getCreateTime(), locale));
                resultMap.put("newAnswerCount", String.valueOf(questionDao.getAnswerAmount(Integer.parseInt(questionId))));
            } catch (PersistException e) {
                logger.error("Answer don't persisted.", e);
                resultMap.put("response", "error");
                resultMap.put("error", bundle.getString("question.something"));
            }
        }

        return new Gson().toJson(resultMap);
    }

    private boolean checkAll(User user, String text, String questionId, Map<String, String> resultMap) {
        if (!checkLogin(user)) {
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("question.not-logged-in"));
            return false;
        }
        if (!checkSelfAnswer(user, questionId)) {
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("question.self-answer"));
            return false;
        }
        if (!checkRepeatAnswer(user, questionId)) {
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("question.repeat-answer"));
            return false;
        }
        if (!checkQuestionId(questionId)) {
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("question.invalid-id"));
            return false;
        }
        if (!checkText(text)) {
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("question.invalid-answer-text"));
            return false;
        }
        return true;
    }

    private boolean checkLogin(User user) {
        return user != null;
    }

    private boolean checkText(String text) {
        return text != null && text.length() >= 10;
    }

    private boolean checkQuestionId(String questionId) {
        if (questionId == null) return false;
        try {
            Integer.parseInt(questionId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkSelfAnswer(User user, String questionId) {
        try {
            return !userDao.userHasQuestion(user.getId(), Integer.parseInt(questionId));
        } catch (PersistException e) {
            logger.error("Bad DAO query", e);
            return false;
        }
    }

    private boolean checkRepeatAnswer(User user, String questionId) {
        try {
            return !userDao.userAnsweredQuestion(user.getId(), Integer.parseInt(questionId));
        } catch (PersistException e) {
            logger.error("Bad DAO query", e);
            return false;
        }
    }

}
