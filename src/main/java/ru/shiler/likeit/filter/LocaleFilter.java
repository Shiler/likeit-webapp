package ru.shiler.likeit.filter;

import ru.shiler.likeit.model.user.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <code>LocaleFilter</code> is a {@link Filter} that
 * sets up default locale if it does not specified.
 * If it is an authorized user the locale is set depend on user's stored locale.
 */
public class LocaleFilter implements Filter {

    private String defaultLocale;
    private List<String> supportedLocales;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        defaultLocale = filterConfig.getInitParameter("defaultLocale");
        supportedLocales = Arrays.asList(filterConfig.getInitParameter("supportedLocales").split("/"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        String locale = (String) session.getAttribute("locale");
        User user = (User) session.getAttribute("USER");
        if (locale == null || !supportedLocales.contains(locale)) {
            session.setAttribute("locale", defaultLocale);
            if (user != null) user.setLocale(defaultLocale);
        } else if (user != null) {
            session.setAttribute("locale", user.getLocale());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
