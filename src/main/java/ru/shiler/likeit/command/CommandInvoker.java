package ru.shiler.likeit.command;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.impl.async.RateAnswerAsyncCommand;
import ru.shiler.likeit.command.impl.page.TopQuestionsPageCommand;
import ru.shiler.likeit.command.impl.action.*;
import ru.shiler.likeit.command.impl.async.AddAnswerAsyncCommand;
import ru.shiler.likeit.command.impl.async.GetLastQuestionsAsyncCommand;
import ru.shiler.likeit.command.impl.async.LikeQuestionAsyncCommand;
import ru.shiler.likeit.command.impl.page.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class CommandInvoker {

    private Map<String, Class> commandMap = new HashMap<>();
    private final static Logger logger = Logger.getLogger(CommandInvoker.class);

    public CommandInvoker() {
        commandMap.put("/app/index", IndexPageCommand.class);
        commandMap.put("/app/search", SearchPageCommand.class);
        commandMap.put("/app/categories", CategoriesPageCommand.class);
        commandMap.put("/app/top", TopQuestionsPageCommand.class);
        commandMap.put("/app/question", QuestionPageCommand.class);
        commandMap.put("/app/setLocale", SetLocaleCommand.class);
        commandMap.put("/app/register", RegisterPageCommand.class);
        commandMap.put("/app/register.do", RegisterCommand.class);
        commandMap.put("/app/logout", LogoutCommand.class);
        commandMap.put("/app/login", LoginPageCommand.class);
        commandMap.put("/app/login.do", LoginCommand.class);
        commandMap.put("/app/question.add", AddQuestionCommand.class);
        commandMap.put("/app/answer.add", AddAnswerAsyncCommand.class);
        commandMap.put("/app/getLastQuestions", GetLastQuestionsAsyncCommand.class);
        commandMap.put("/app/like", LikeQuestionAsyncCommand.class);
        commandMap.put("/app/answer.rate", RateAnswerAsyncCommand.class);
        commandMap.put("/app/profile", UserPageCommand.class);
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = getCommandName(request);
        try {
            Class commandClass = commandMap.get(commandName);
            if (commandClass != null) {
                SimpleCommand command = (SimpleCommand) commandClass.newInstance();
                command.execute(request, response);
                if (command instanceof AbstractCommand) {
                    ((AbstractCommand) command).complete();
                }
            } else if (commandName.startsWith("/app/")) {
                ((SimpleCommand) commandMap.get("/app/index").newInstance()).execute(request, response);
            }
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (InstantiationException e) {
            logger.error("Command instance creation error", e);
        }
    }

    private String getCommandName(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("?")) {
            int pos = requestURI.indexOf("?");
            return requestURI.substring(0, pos - 1);
        }
        return request.getRequestURI();
    }

}
