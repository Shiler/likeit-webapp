package ru.shiler.likeit.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Defines a Command Pattern object to handle a client request information
 * provided by {@link javax.servlet.http.HttpServletRequest}. Business logic class.
 *
 * @see javax.servlet.http.HttpServletRequest
 */
public interface SimpleCommand {

    /**
     * Executes command which specified by implementing this interface
     *
     * @param request  a <code>HttpServletRequest</code> object specifying client request
     * @param response a <code>HttpServletResponse</code> object assisting a servlet
     *                 in sending a response to the client
     * @throws ServletException
     * @throws IOException
     */
    void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
