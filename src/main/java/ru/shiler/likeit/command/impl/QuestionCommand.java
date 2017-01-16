package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.answer.MySqlAnswerDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.answer.Answer;
import ru.shiler.likeit.model.question.Question;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class QuestionCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("id");
        if (query != null) {
            DaoFactory daoFactory = new MySqlDaoFactory();
            Question question;
            List<Answer> answers;
            try {
                MySqlQuestionDao questionDao = (MySqlQuestionDao) daoFactory.getDao(daoFactory.getContext(), Question.class);
                MySqlAnswerDao answerDao = (MySqlAnswerDao) daoFactory.getDao(daoFactory.getContext(), Answer.class);
                question = questionDao.getByPK(Integer.parseInt(query));
                answers = answerDao.getByQuestionId(question.getId());
                request.setAttribute("question", question);
                request.setAttribute("answers", answers);
                request.getRequestDispatcher(CommandPath.QUESTION).forward(request, response);
            } catch (PersistException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/index");
        }
    }
}
