package ru.shiler.likeit.command;

import org.apache.log4j.Logger;
import ru.shiler.likeit.database.ConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Extends the {@link SimpleCommand} interface to provide
 * thread-safe way for working with database.
 * <code>Connection</code> is taken from the Tomcat <code>DataSource</code>
 * and is used only by a single command. After the <code>Command</code> execution
 * the <code>Connection</code> is automatically closed by the {@link CommandInvoker} instance.
 */
public abstract class AbstractCommand implements SimpleCommand {

    /**
     * Logger variable.
     */
    private final static Logger logger = Logger.getLogger(AbstractCommand.class);

    /**
     * A <code>Connection</code> instance for each <code>AbstractCommand</code>
     */
    private Connection connection;

    /**
     * Initializes <code>Connection</code> by the link from ServletContainer's DataSource
     */
    public AbstractCommand() {
        connection = ConnectionPool.getConnection();
    }

    /**
     * @return {@link java.sql.Connection} getter
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the <code>connection</code> if it exists.
     */
    public void complete() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }
    }

    /**
     * Inherited method from {@link SimpleCommand}. To be implemented for each type of command.
     *
     * @param request  a <code>HttpServletRequest</code> object specifying client request
     * @param response a <code>HttpServletResponse</code> object assisting a servlet
     *                 in sending a response to the client
     * @throws ServletException
     * @throws IOException
     */
    public abstract void execute(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException;

}
