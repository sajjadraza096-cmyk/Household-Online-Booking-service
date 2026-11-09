package com.meticulous.controller;

import java.io.IOException;

import com.meticulous.dao.UserDAO;
import com.meticulous.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    // GET /register  ->  show the registration form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    // POST /register  ->  save the new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Server-side validation
        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || mobile == null || !mobile.matches("\\d{10}")
                || password == null || password.length() < 6) {
            sendError(request, response,
                    "Please fill all fields correctly. Mobile must be 10 digits and password at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            sendError(request, response, "Passwords do not match.");
            return;
        }

        try {
            // 2. Block duplicate emails
            if (userDAO.emailExists(email.trim())) {
                sendError(request, response, "This email is already registered. Please login.");
                return;
            }

            // 3. Save the user (every self-registered user is a CUSTOMER)
            User user = new User(name.trim(), email.trim(), mobile.trim(), password, "CUSTOMER");
            boolean saved = userDAO.register(user);

            if (saved) {
                response.sendRedirect("login.jsp?msg=registered");
            } else {
                sendError(request, response, "Registration failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError(request, response, "Something went wrong on the server. Please try again.");
        }
    }

    // Helper: send the user back to the form with an error message
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
