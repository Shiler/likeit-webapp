package ru.shiler.likeit.command.impl.page;

import ru.shiler.likeit.command.SimpleCommand;
import ru.shiler.likeit.util.CommandUtils;
import ru.shiler.likeit.constants.JspPath;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class RegisterPageCommand implements SimpleCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("USER") != null) {
            response.sendRedirect(CommandUtils.getBackUri(request));
            return;
        }
        request.getRequestDispatcher(JspPath.REGISTER).forward(request, response);
    }
}
