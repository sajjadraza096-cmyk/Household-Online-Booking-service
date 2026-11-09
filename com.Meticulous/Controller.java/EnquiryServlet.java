package com.meticulous.controller;

import java.io.IOException;

import com.meticulous.dao.EnquiryDAO;
import com.meticulous.model.Enquiry;
import com.meticulous.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/enquiry")
public class EnquiryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private EnquiryDAO enquiryDAO = new EnquiryDAO();

    // ---------- Helpers ----------
    private User getLoggedUser(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return null;
        }
        return (User) request.getSession(false).getAttribute("user");
    }

    // Returns true if the user is an admin. Otherwise sends the right response and returns false.
    private boolean requireAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?msg=loginrequired");
            return false;
        }
        if (!"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    // ---------- GET: admin list ----------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireAdmin(request, response)) {
            return;
        }

        try {
            request.setAttribute("enquiries", enquiryDAO.selectAll());
            request.getRequestDispatcher("/WEB-INF/views/enquiry-list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- POST: insert (public) and delete (admin) ----------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {

                case "insert":
                    insertEnquiry(request, response);
                    break;

                case "delete": {
                    if (!requireAdmin(request, response)) {
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    enquiryDAO.delete(id);
                    response.sendRedirect("enquiry?msg=deleted");
                    break;
                }

                default:
                    response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("enquiry?msg=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- Public enquiry form from the home page ----------
    private void insertEnquiry(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String ctx = request.getContextPath();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String service = request.getParameter("service");

        name = (name == null) ? "" : name.trim();
        email = (email == null) ? "" : email.trim();
        mobile = (mobile == null) ? "" : mobile.trim();
        service = (service == null) ? "" : service.trim();

        // Server-side validation (the form is public, so we trust nothing)
        boolean valid = !name.isEmpty() && name.length() <= 100
                && email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$") && email.length() <= 100
                && mobile.matches("\\d{10}")
                && !service.isEmpty() && service.length() <= 150;

        if (!valid) {
            response.sendRedirect(ctx + "/home?enquiry=invalid#contact");
            return;
        }

        enquiryDAO.insert(new Enquiry(name, email, mobile, service));
        response.sendRedirect(ctx + "/home?enquiry=sent#contact");   // Post-Redirect-Get
    }
}
