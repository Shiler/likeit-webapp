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
 * Created by Evgeny Yushkevich on 20.01.2017.
 */
public abstract class AbstractCommand implements SimpleCommand {

    private final static Logger logger = Logger.getLogger(AbstractCommand.class);

    private Connection connection;

    public AbstractCommand() {
        connection = ConnectionPool.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public void complete() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warn("Unable to close connection", e);
        }
    }

    public abstract void execute(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException;

}
