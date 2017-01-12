package ru.shiler.likeit.filter;

import org.apache.commons.lang3.StringUtils;
import ru.shiler.likeit.model.user.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class AuthFilter implements Filter {

    private List<String> pathFilters = Arrays.asList("add", "delete", "buy", "deposit", "profile");

    public AuthFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        String path = StringUtils.substringAfterLast(uri, "/");

        if (!pathFilters.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = ((HttpServletRequest) request).getSession();
        User user = (User) session.getAttribute("PRINCIPAL");
        if (user != null) {
            chain.doFilter(request, response);
            return;
        }

        ((HttpServletResponse) response).sendRedirect("/login");
    }

    @Override
    public void destroy() {

    }
}
