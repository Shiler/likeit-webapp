package ru.shiler.likeit.command;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.impl.ShowIndexPageCommand;
import ru.shiler.likeit.command.impl.SearchCommand;

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

    private final static Logger logger = Logger.getLogger(CommandInvoker.class);

    private Map<String, Command> commandMap = new HashMap<>();

    public CommandInvoker() {
        commandMap.put("/app/index", new ShowIndexPageCommand());
        commandMap.put("/app/search", new SearchCommand());
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = getCommandName(request);
        Command command = commandMap.get(commandName);
        if (command != null) {
            command.execute(request, response);
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
