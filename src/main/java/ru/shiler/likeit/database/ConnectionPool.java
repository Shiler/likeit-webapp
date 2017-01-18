package ru.shiler.likeit.database;

import org.apache.log4j.Logger;
import ru.shiler.likeit.database.exception.PersistException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Evgeny Yushkevich on 17.01.2017.
 */
public class ConnectionPool {

    private static InitialContext INITIAL_CONTEXT;
    private static DataSource DATA_SOURCE;

    private final static Logger logger = Logger.getLogger(ConnectionPool.class);

    static {
        try {
            INITIAL_CONTEXT = new InitialContext();
            DATA_SOURCE = (DataSource) INITIAL_CONTEXT.lookup("java:comp/env/jdbc/likeit");
        } catch (NamingException e) {
            logger.error("Unable to get DataSource", e);
        }

    }

    public static Connection getConnection() {
        try {
            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            logger.error("Unable to get connection", e);
        }
        return null;
    }

}
