package ru.shiler.likeit.command;

import ru.shiler.likeit.command.impl.*;
import ru.shiler.likeit.command.impl.action.*;
import ru.shiler.likeit.command.impl.async.AddAnswerAsyncCommand;
import ru.shiler.likeit.command.impl.async.GetLastQuestionsAsyncCommand;
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

    private Map<String, SimpleCommand> commandMap = new HashMap<>();

    public CommandInvoker() {
        commandMap.put("/app/index", new IndexPageCommand());
        commandMap.put("/app/search", new SearchPageCommand());
        commandMap.put("/app/categories", new CategoriesPageCommand());
        commandMap.put("/app/top", new TopQuestionsCommand());
        commandMap.put("/app/question", new QuestionPageCommand());
        commandMap.put("/app/setLocale", new SetLocaleCommand());
        commandMap.put("/app/register", new RegisterPageCommand());
        commandMap.put("/app/register.do", new RegisterCommand());
        commandMap.put("/app/logout", new LogoutCommand());
        commandMap.put("/app/login", new LoginPageCommand());
        commandMap.put("/app/login.do", new LoginCommand());
        commandMap.put("/app/question.add", new AddQuestionCommand());
        commandMap.put("/app/answer.add", new AddAnswerAsyncCommand());
        commandMap.put("/app/getLastQuestions", new GetLastQuestionsAsyncCommand());
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = getCommandName(request);
        SimpleCommand command = commandMap.get(commandName);
        if (command != null) {
            command.execute(request, response);
            if(command instanceof AbstractCommand) {
                ((AbstractCommand) command).complete();
            }
        } else if (commandName.startsWith("/app/"))  {
            commandMap.get("/app/index").execute(request, response);
        }
    }

    private String getCommandName(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("?")) {
            int pos = requestURI.indexOf("?");
            return requestURI.substring(0, pos-1);
        }
        return request.getRequestURI();
    }

}
