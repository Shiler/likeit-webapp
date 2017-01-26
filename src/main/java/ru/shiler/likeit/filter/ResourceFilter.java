package ru.shiler.likeit.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <code>ResourceFilter</code> is a {@link Filter} that separates
 * requests to the static resources from requests to the {@link ru.shiler.likeit.controller.AppController}
 */
public class ResourceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/resources/")) {
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/app" + path).forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
