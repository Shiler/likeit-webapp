package ru.shiler.likeit.command.impl.page;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.constants.JspPath;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class QuestionPageCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(QuestionPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("id");
        if (query != null) {
            DaoFactory daoFactory = new MySqlDaoFactory();
            Question question;
            List<Answer> answers;
            try {
                MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(getConnection(), Question.class);
                MySqlAnswerDao answerDao = (MySqlAnswerDao) daoFactory.getDao(getConnection(), Answer.class);
                question = questionDao.getByPK(Integer.parseInt(query));
                answers = answerDao.getByQuestionId(question.getId());
                User user = (User) request.getSession().getAttribute("USER");
                boolean liked = false;
                if (user != null) {
                    liked = questionDao.isUserRated(user.getId(), question.getId());
                }
                request.setAttribute("liked", liked);
                request.setAttribute("question", question);
                request.setAttribute("answers", answers);
                request.getRequestDispatcher(JspPath.QUESTION).forward(request, response);
            } catch (PersistException e) {
                logger.error("Error in DAO queries", e);
            }
        } else {
            response.sendRedirect("/index");
        }
    }
}
