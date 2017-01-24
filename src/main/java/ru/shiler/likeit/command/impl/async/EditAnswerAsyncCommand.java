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
 * Created by Evgeny Yushkevich on 24.01.2017.
 */
public class EditAnswerAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(EditAnswerAsyncCommand.class);

    private DaoFactory daoFactory;
    private MySqlAnswerDao answerDao;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String answerIdStr = request.getParameter("id");
        String textStr = request.getParameter("text");
        initDao();
        User user = (User) request.getSession().getAttribute("USER");
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (checkParameters(answerIdStr, textStr, user) && update(answerIdStr, textStr)) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "false");
        }
        response.getWriter().write(toJson(resultMap));
    }

    private boolean update(String answerIdStr, String textStr) {
        try {
            Answer answer = answerDao.getByPK(Integer.parseInt(answerIdStr));
            answer.setText(textStr);
            answerDao.update(answer);
            return true;
        } catch (PersistException e) {
            logger.error("Invalid answer ID", e);
            return false;
        }
    }

    private boolean checkParameters(String answerIdStr, String textStr, User user) {
        if (answerIdStr == null || textStr == null) {
            return false;
        }
        return checkId(user, answerIdStr) && checkText(textStr);
    }

    private boolean checkId(User user, String idStr) {
        if (user == null) return false;
        try {
            Answer answer = answerDao.getByPK(Integer.parseInt(idStr));
            return answer.getCreator().getId() == user.getId();
        } catch (PersistException e) {
            logger.error("Invalid answer ID", e);
            return false;
        }
    }

    private boolean checkText(String textStr) {
        return textStr.length() >= 10;
    }

    private void initDao() {
        daoFactory = new MySqlDaoFactory();
        try {
            answerDao = (MySqlAnswerDao) daoFactory.getDao(getConnection(), Answer.class);
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
        }
    }

    private String toJson(Map<String, String> map) {
        return new Gson().toJson(map);
    }

}
