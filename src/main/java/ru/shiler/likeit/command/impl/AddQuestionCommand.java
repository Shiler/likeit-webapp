package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.command.CommandUtils;
import ru.shiler.likeit.constants.CommandPath;
import ru.shiler.likeit.database.ConnectionPool;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionDao;
import ru.shiler.likeit.database.dao.impl.question.MySqlQuestionTypeDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.question.Question;
import ru.shiler.likeit.model.question.QuestionType;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class AddQuestionCommand implements Command {

    private final static Logger logger = Logger.getLogger(AddQuestionCommand.class);
    private Connection connection;
    private MySqlQuestionDao questionDao;
    private MySqlQuestionTypeDao questionTypeDao;
    private String backUri;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initDao(request, response);
        backUri = CommandUtils.getBackUri(request);
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String content = request.getParameter("content");
        User user = (User) request.getSession().getAttribute("USER");
        if (!doAllChecks(title, category, content)) {
            System.out.println("something goes wrong");
            response.sendRedirect(backUri);
            return;
        }
        Question question = composeQuestion(title, category, content, user);
        persist(question, request, response);
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }
    }

    private void persist(Question question, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Question persistedQuestion = questionDao.persist(question);
            response.sendRedirect("/question?id=" + persistedQuestion.getId());
        } catch (PersistException e) {
            logger.error("Exception while persisting question", e);
            request.getRequestDispatcher(CommandPath.ERROR).forward(request, response);
        }
    }

    private Question composeQuestion(String title, String category, String content, User user) {
        Question question = new Question();
        question.setTitle(title);
        try {
            question.setQuestionType(questionTypeDao.getByName(category));
        } catch (PersistException e) {
            logger.error("Unable to get question type by DAO", e);
        }
        question.setContent(content);
        question.setCreator(user);
        question.setCreateTime(new Timestamp(new Date().getTime()));
        return question;
    }

    private void initDao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        connection = ConnectionPool.getConnection();
        DaoFactory daoFactory = new MySqlDaoFactory();
        try {
            questionDao = (MySqlQuestionDao) daoFactory.getDao(connection, Question.class);
            questionTypeDao = (MySqlQuestionTypeDao) daoFactory.getDao(connection, QuestionType.class);
        } catch (PersistException e) {
            logger.error("DAO queries error", e);
            request.getRequestDispatcher(CommandPath.ERROR).forward(request, response);
        }
    }

    private boolean doAllChecks(String title, String category, String content) {
        return checkTitle(title) && checkCategory(category) && checkContent(content);
    }

    private boolean checkTitle(String title) {
        return title != null && title.length() >= 10;
    }

    private boolean checkCategory(String category) {
        try {
            System.out.println(questionTypeDao.isExists(category));
            return questionTypeDao.isExists(category);
        } catch (PersistException e) {
            logger.error("DAO execution error", e);
            return false;
        }
    }

    private boolean checkContent(String content) {
        return content != null && content.length() >= 10;
    }

}
