package ru.shiler.likeit.command.impl.async;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.util.TimestampUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Evgeny Yushkevich on 20.01.2017.
 */
public class GetLastQuestionsAsyncCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(GetLastQuestionsAsyncCommand.class);

    private DaoFactory daoFactory;
    private MySqlQuestionDao questionDao;
    String locale;
    private ResourceBundle bundle;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        initDao();
        locale = (String) request.getSession().getAttribute("locale");
        bundle = ResourceBundle.getBundle("app-strings", new Locale(locale));
        String jsonResponse = formResponse();
        response.getWriter().write(jsonResponse);
    }

    private String formResponse() {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        try {
            List<Question> lastQuestions = questionDao.getLastQuestions(6);
            resultMap.put("response", "success");
            putQuestions(resultMap, lastQuestions);
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
            resultMap.put("response", "error");
            resultMap.put("error", bundle.getString("index.no-db-conn"));
        }
        return new Gson().toJson(resultMap);
    }

    private void putQuestions(Map<String, Object> resultMap, List<Question> questions) {
        List<Map> questionList = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Map<String, String> questionMap = new LinkedHashMap<>();
            questionMap.put("id", String.valueOf(question.getId()));
            questionMap.put("createTime", TimestampUtils.formatTimestamp(question.getCreateTime(), locale));
            questionMap.put("title", question.getTitle());
            questionMap.put("creator", question.getCreator().getFullName());
            questionList.add(questionMap);
        }
        resultMap.put("questions", questionList);
        
    }


    private void initDao() {
        daoFactory = new MySqlDaoFactory();
        try {
            questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
        } catch (PersistException e) {
            logger.error("DAO creator error.", e);
        }
    }
}
