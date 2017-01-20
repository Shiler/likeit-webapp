package ru.shiler.likeit.command.impl.action;

import ru.shiler.likeit.command.SimpleCommand;
import ru.shiler.likeit.util.CommandUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class LogoutCommand implements SimpleCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(CommandUtils.getBackUri(request));
    }
}
