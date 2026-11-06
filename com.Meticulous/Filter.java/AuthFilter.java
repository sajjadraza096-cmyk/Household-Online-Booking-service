package com.meticulous.filter;

import java.io.IOException;

import com.meticulous.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Runs before these URLs reach their servlets
@WebFilter(urlPatterns = {"/booking", "/service"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String ctx = request.getContextPath();

        // 1. Stop the browser from caching these pages
        //    (so the Back button cannot show private pages after logout)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 2. Must be logged in
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(ctx + "/login?msg=loginrequired");
            return;
        }

        // 3. /service is for ADMIN only
        String path = request.getServletPath();
        if (path.equals("/service") && !"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);   // shows the 403 error page
            return;
        }

        // 4. All good, continue to the servlet
        chain.doFilter(req, res);
    }
}
