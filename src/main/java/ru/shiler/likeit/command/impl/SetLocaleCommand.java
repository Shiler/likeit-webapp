package ru.shiler.likeit.command.impl;

import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.command.CommandUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class SetLocaleCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();
        if (lang != null) {
            session.setAttribute("locale", lang);
        }
        //// TODO: 16.01.2017 IMPLEMENT USER LOCALE
        response.sendRedirect(CommandUtils.getBackUri(request));
    }
}
