package ru.shiler.likeit.command.impl.async;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Evgeny Yushkevich on 23.01.2017.
 */
public class RateAnswerAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(RateAnswerAsyncCommand.class);

    private int answerId;
    private int rate;
    private User user;
    private DaoFactory daoFactory;
    private MySqlAnswerDao answerDao;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (request.getParameter("answerId") != null
                && request.getParameter("rate") != null) {
            answerId = Integer.parseInt(request.getParameter("answerId"));
            rate = Integer.parseInt(request.getParameter("rate"));
        } else {
            resultMap.put("result", "false");
            response.getWriter().print(toJson(resultMap));
            return;
        }
        user = (User) request.getSession().getAttribute("USER");
        initDao();
        if (checkRate() && checkSelfAnswer(user)) {
            rate(resultMap);
        } else {
            resultMap.put("result", "false");
        }
        response.getWriter().print(toJson(resultMap));
    }

    private String toJson(Map<String, String> resultMap) {
        return new Gson().toJson(resultMap);
    }

    private void rate(Map<String, String> resultMap) {
        try {
            answerDao.rate(answerId, user.getId(), rate);
            Answer answer = answerDao.getByPK(answerId);
            resultMap.put("result", "success");
            resultMap.put("newRating", String.valueOf(answer.getRating()));
            resultMap.put("newVoteCount", String.valueOf(answer.getVoteCount()));
        } catch (PersistException e) {
            logger.error("Answer rate error", e);
            resultMap.put("result", "false");
        }
    }

    private void initDao() {
        daoFactory = new MySqlDaoFactory();
        try {
            answerDao = (MySqlAnswerDao) daoFactory.getDao(getConnection(), Answer.class);
        } catch (PersistException e) {
            logger.error(e);
        }
    }

    private boolean checkRate() {
        return (rate >= 1 && rate <= 5);
    }

    private boolean checkSelfAnswer(User user) {
        if (user == null) {
            return false;
        }
        try {
            Answer answer = answerDao.getByPK(answerId);
            return answer.getCreator().getId() != user.getId();
        } catch (PersistException e) {
            logger.error(e);
            return false;
        }
    }

}
