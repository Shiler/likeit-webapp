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

/**
 * Master controller to implementation of one-servlet web app model.
 */
public class AppController extends HttpServlet {

    /**
     * A {@link CommandInvoker} instance which handles incoming requests from clients.
     */
    private CommandInvoker invoker;

    /**
     * Default constructor. <code>CommandInvoker</code> is initialized.
     */
    public AppController() {
        invoker = new CommandInvoker();
    }

    /**
     * Transfers request handling to the {@link CommandInvoker} instance.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        invoker.invoke(req, resp);
    }

    /**
     * The same as <code>doGet</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        invoker.invoke(req, resp);
    }
}
