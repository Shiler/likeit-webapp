package ru.shiler.likeit.command.impl.action;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.AbstractCommand;
import ru.shiler.likeit.util.CommandUtils;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class SetLocaleCommand extends AbstractCommand {

    private final static Logger logger = Logger.getLogger(SetLocaleCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();

        if (lang != null) {
            session.setAttribute("locale", lang);
        }

        User user = (User) session.getAttribute("USER");
        if (user != null && !user.getLocale().equals(lang)) {
            user.setLocale(lang);
            DaoFactory daoFactory = new MySqlDaoFactory();
            try {
                MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(getConnection(), User.class);
                userDao.update(user);
            } catch (PersistException e) {
                logger.error("DAO not worked properly", e);
            }
        }

        response.sendRedirect(CommandUtils.getBackUri(request));
    }

}
