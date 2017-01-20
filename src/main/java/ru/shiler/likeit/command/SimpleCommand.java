package ru.shiler.likeit.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */


public interface SimpleCommand {
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
