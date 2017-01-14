package ru.shiler.likeit.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
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
            chain.doFilter(request, response); // Goes to default servlet.
        } else {
            request.getRequestDispatcher("/app" + path).forward(request, response); // Goes to your controller.
        }
    }

    @Override
    public void destroy() {

    }
}
