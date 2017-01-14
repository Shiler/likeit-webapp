package ru.shiler.likeit.controller;

import ru.shiler.likeit.command.CommandInvoker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class AppController extends HttpServlet {

    private CommandInvoker invoker;

    public AppController() {
        invoker = new CommandInvoker();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        invoker.invoke(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
