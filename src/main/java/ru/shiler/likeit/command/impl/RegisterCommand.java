package ru.shiler.likeit.command.impl;

import org.apache.log4j.Logger;
import ru.shiler.likeit.command.Command;
import ru.shiler.likeit.constants.Login;
import ru.shiler.likeit.database.dao.DaoFactory;
import ru.shiler.likeit.database.dao.impl.MySqlDaoFactory;
import ru.shiler.likeit.database.dao.impl.user.MySqlUserDao;
import ru.shiler.likeit.database.exception.PersistException;
import ru.shiler.likeit.model.user.User;
import ru.shiler.likeit.model.user.UserRole;
import ru.shiler.likeit.security.Sha256;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class RegisterCommand implements Command {

    private final static Logger logger = Logger.getLogger(RegisterCommand.class);

    private final List<String> requiredParams = Arrays.asList(
            "email", "username", "fullName", "age", "password");

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().toLowerCase().equals("get")) {
            response.sendRedirect("/index");
            return;
        }

        Set keySet = request.getParameterMap().keySet();
        for (String parameter : requiredParams) {
            if (!keySet.contains(parameter)) {
                response.sendRedirect("/register?error=required_fault");
                return;
            }
        }
        if (!doAllParameterChecks(request)) {
            response.sendRedirect("/register?error=invalid_parameters");
            return;
        }
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String age = request.getParameter("age");
        String password = request.getParameter("password");
        DaoFactory daoFactory = new MySqlDaoFactory();
        try {
            MySqlUserDao userDao = (MySqlUserDao) daoFactory.getDao(daoFactory.getContext(), User.class);
            if (isUserExists(username, email, userDao)) {
                response.sendRedirect("/register?error=user_already_exists");
                return;
            }
            createUser(request, response, userDao);
        } catch (PersistException e) {
            logger.error("Can't check if user exists from database", e);
        }

    }

    private User buildUserFromRequest(HttpServletRequest req) {
        User user = new User();
        user.setEmail(req.getParameter("email"));
        user.setUserName(req.getParameter("username"));
        user.setFullName(req.getParameter("fullName"));
        user.setAge(Integer.parseInt(req.getParameter("age")));
        user.setPassword(Sha256.encrypt(req.getParameter("password")));
        user.setCreateTime(new Timestamp(new Date().getTime()));
        UserRole userRole = new UserRole();
        userRole.setId(1);
        user.setUserRole(userRole);
        return user;
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response, MySqlUserDao userDao) throws IOException {
        User user = buildUserFromRequest(request);
        try {
            userDao.persist(user);
            request.getSession().setAttribute(Login.SESSION_USER_ATTRIBUTE, user);

            response.sendRedirect("/index");
            return;
        } catch (PersistException e) {
            response.sendRedirect("/register?error=unknown");
            logger.error("Error while persisting User. ", e);
        }
    }

    private boolean isUserExists(String username, String email, MySqlUserDao userDao) {
        try {
            if (userDao.getByEmail(email) != null || userDao.getByUserName(username) != null) {
                return true;
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean doAllParameterChecks(HttpServletRequest request) {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String age = request.getParameter("age");
        String password = request.getParameter("password");
        return checkEmail(email) && checkUsername(username) &&
                checkFullName(fullName) && checkAge(age) && checkPassword(password);
    }

    private boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,10})");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean checkFullName(String fullName) {
        Pattern pattern = Pattern.compile("([а-яА-Яa-zA-Z\\s]{6,50})");
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    private boolean checkUsername(String username) {
        return checkLatin6Symb(username);
    }

    private boolean checkAge(String age) {
        Pattern pattern = Pattern.compile("([0-9]{1,3})");
        Matcher matcher = pattern.matcher(age);
        return matcher.matches();
    }

    private boolean checkPassword(String password) {
        return checkLatin6Symb(password);
    }

    private boolean checkLatin6Symb(String str) {
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{6,30})");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
