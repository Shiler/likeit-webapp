package ru.shiler.likeit.command;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.impl.action.*;
import ru.shiler.likeit.command.impl.async.*;
import ru.shiler.likeit.command.impl.page.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Command Pattern class which is the one that executes
 * any {@link SimpleCommand} Command that is set to it.
 *
 * @see ru.shiler.likeit.command.SimpleCommand
 */
public class CommandInvoker {

    /**
     * Map contains all of commands known by this application
     */
    private Map<String, Class> commandMap = new HashMap<>();

    /**
     * Logger variable
     */
    private final static Logger logger = Logger.getLogger(CommandInvoker.class);

    /**
     * Initializes <code>commandMap</code> variable with known URI patterns
     * and their corresponding classes.
     */
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
        commandMap.put("/app/answer.delete", DeleteAnswerAsyncCommand.class);
        commandMap.put("/app/answer.edit", EditAnswerAsyncCommand.class);
    }

    /**
     * Creates an instance of a {@link SimpleCommand} class and executes it.
     * If the command parent class is defined as {@link AbstractCommand}
     * which uses DAO objects, completes it by calling <code>complete();</code> method.
     *
     * @param request  a <code>HttpServletRequest</code> object specifying client request
     * @param response a <code>HttpServletResponse</code> object assisting a servlet
     *                 in sending a response to the client
     * @throws ServletException if a {@link javax.servlet.RequestDispatcher} can't
     *                          dispatch JSP path
     * @throws IOException
     * @see ru.shiler.likeit.command.SimpleCommand
     * @see ru.shiler.likeit.command.AbstractCommand
     */
    public void invoke(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
                ((SimpleCommand) commandMap.get("/app/index")
                        .newInstance()).execute(request, response);
            }
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (InstantiationException e) {
            logger.error("Command instance creation error", e);
        }
    }

    /**
     * Selects <code>String</code> containing command name from {@link HttpServletRequest}
     * URI. If request URI contains any parameters they will be trimmed.
     *
     * @param request a <code>HttpServletRequest</code> containing request URI
     * @return <code>String</code> containing command name from <code>HttpServletRequest</code>
     */
    private String getCommandName(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("?")) {
            int pos = requestURI.indexOf("?");
            return requestURI.substring(0, pos - 1);
        }
        return request.getRequestURI();
    }

}
