package com.meticulous.controller;

import java.io.IOException;

import com.meticulous.dao.UserDAO;
import com.meticulous.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    // GET /login  ->  show the login form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // POST /login  ->  check credentials and start a session
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 1. Basic validation
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            sendError(request, response, "Please enter both email and password.");
            return;
        }

        try {
            // 2. Ask the DAO to check the credentials
            User user = userDAO.login(email.trim(), password);

            if (user == null) {
                sendError(request, response, "Invalid email or password.");
                return;
            }

            // 3. Login OK: discard any old session and create a fresh one
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);

            user.setPassword(null);                 // never keep the password hash in the session
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // session expires after 30 minutes

            // 4. Redirect to the home page
            response.sendRedirect("home");

        } catch (Exception e) {
            e.printStackTrace();
            sendError(request, response, "Something went wrong on the server. Please try again.");
        }
    }

    // Helper: send the user back to the login form with an error message
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
