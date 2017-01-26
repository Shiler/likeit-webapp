package ru.shiler.likeit.database;

import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A wrapper class working with application's <code>InitialContext</code>
 * and its <code>DataSource</code>
 */
public class ConnectionPool {

    /**
     * {@link InitialContext} static variable
     */
    private static InitialContext INITIAL_CONTEXT;

    /**
     * {@link DataSource} static variable
     */
    private static DataSource DATA_SOURCE;

    /**
     * Logger variable
     */
    private final static Logger logger = Logger.getLogger(ConnectionPool.class);

    /**
     * Static initialization of the Pool variables.
     * DataSource is obtained from JDBC driver
     */
    static {
        try {
            INITIAL_CONTEXT = new InitialContext();
            DATA_SOURCE = (DataSource) INITIAL_CONTEXT.lookup("java:comp/env/jdbc/likeit");
        } catch (NamingException e) {
            logger.error("Unable to get DataSource", e);
        }

    }

    /**
     * Returns a new <code>Connection</code> from Pool if it is possible.
     *
     * @return {@link java.sql.Connection} new thread-safe database connection
     */
    public static Connection getConnection() {
        try {
            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            logger.error("Unable to get connection", e);
        }
        return null;
    }

}
