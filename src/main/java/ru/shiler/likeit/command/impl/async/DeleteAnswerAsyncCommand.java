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
 * Created by Evgeny Yushkevich on 22.01.2017.
 */
public class DeleteAnswerAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(DeleteAnswerAsyncCommand.class);

    private DaoFactory daoFactory;
    private MySqlAnswerDao answerDao;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> resultMap = new LinkedHashMap<>();
        String answerIdStr = request.getParameter("id");
        if (answerIdStr == null) {
            resultMap.put("result", "false");
            response.getWriter().print(toJson(resultMap));
            return;
        }
        int answerId = Integer.parseInt(answerIdStr);
        initDao();
        User user = (User) request.getSession().getAttribute("USER");
        if (checkUser(user, answerId)) {
            delete(answerId, resultMap);
        } else {
            resultMap.put("result", "false");
        }
        response.getWriter().print(toJson(resultMap));
    }

    private void delete(int answerId, Map<String, String> map) {
        try {
            Answer answer = answerDao.getByPK(answerId);
            int newCount = answer.getQuestion().getAnswerAmount() - 1;
            answerDao.delete(answerId);
            map.put("result", "success");
            map.put("newAnswerCount", String.valueOf(newCount));
        } catch (PersistException e) {
            map.put("result", "false");
            logger.error("Answer delete error", e);
        }
    }

    private boolean checkUser(User user, int answerId) {
        if (user == null) {
            return false;
        }
        try {
            Answer answer = answerDao.getByPK(answerId);
            return answer.getCreator().getId() == user.getId();
        } catch (PersistException e) {
            logger.error(e);
            return false;
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

    private String toJson(Map<String, String> map) {
        return new Gson().toJson(map);
    }
}
