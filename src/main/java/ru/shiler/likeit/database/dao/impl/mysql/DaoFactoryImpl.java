package ru.shiler.likeit.database.dao.impl.mysql;

import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.GenericDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Evgeny Yushkevich on 22.11.2016.
 */
public class DaoFactoryImpl implements DaoFactory<Connection> {

    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/likeit!";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private Map<Class, DaoCreator> creators;

    public Connection getContext() throws PersistException {
        Connection connection;
        try {
            Properties info = new Properties();
            info.setProperty("user", user);
            info.setProperty("password", password);
            info.setProperty("useSSL", "true");
            info.setProperty("serverSslCert", "classpath:server.crt");
            info.setProperty("useLegacyDatetimeCode", "false");
            info.setProperty("serverTimezone", "Europe/Moscow");
            connection = DriverManager.getConnection(url, info);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public DaoFactoryImpl() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creators = new HashMap<Class, DaoCreator>();
        creators.put(User.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new UserDao(connection);
            }
        });
        creators.put(User.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new UserDao(connection);
            }
        });
    }

}
