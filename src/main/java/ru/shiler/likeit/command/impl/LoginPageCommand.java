package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.command.CommandUtils;
import ru.shiler.likeit.constants.CommandPath;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class LoginPageCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("USER") != null) {
            response.sendRedirect(CommandUtils.getBackUri(request));
            return;
        }
        request.getRequestDispatcher(CommandPath.LOGIN).forward(request, response);
    }
}
