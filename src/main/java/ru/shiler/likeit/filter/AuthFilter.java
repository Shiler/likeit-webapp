package ru.shiler.likeit.filter;

import ru.shiler.likeit.model.user.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Prevents unauthorized access to protected resources and actions
 */
public class AuthFilter implements Filter {

    private List<String> pathFilters = Arrays.asList(".add", ".edit", ".delete");
    private List<String> ajaxExclusion = Arrays.asList("answer.add");

    public AuthFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();

        for (String pathFilter : pathFilters) {
            if (!isProtectedCommand(uri)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = ((HttpServletRequest) request).getSession();
        User user = (User) session.getAttribute("USER");
        if (user != null) {
            chain.doFilter(request, response);
            return;
        }

        ((HttpServletResponse) response).sendRedirect("/login");
    }

    private boolean isProtectedCommand(String uri) {
        for (String pathFilter : pathFilters) {
            if (uri.contains(pathFilter)) {
                for (String exclusion : ajaxExclusion) {
                    if (uri.contains(exclusion)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
