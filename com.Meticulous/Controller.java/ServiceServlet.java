package com.meticulous.controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import com.meticulous.dao.ServiceDAO;
import com.meticulous.model.Service;
import com.meticulous.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/service")
public class ServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<String> CATEGORIES = Arrays.asList("CLEANING", "HANDYMAN", "GARDEN");
    private ServiceDAO serviceDAO = new ServiceDAO();

    // ---------- Admin check (runs before every request) ----------
    private boolean isAdmin(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return false;
        }
        User user = (User) request.getSession(false).getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?msg=loginrequired");
            return;
        }
        super.service(request, response);   // continue to doGet / doPost
    }

    // ---------- GET: list, new, edit ----------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    request.setAttribute("categories", CATEGORIES);
                    request.getRequestDispatcher("/WEB-INF/views/service-form.jsp").forward(request, response);
                    break;

                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Service existing = serviceDAO.selectById(id);
                    if (existing == null) {
                        response.sendRedirect("service?action=list&msg=notfound");
                        return;
                    }
                    request.setAttribute("service", existing);
                    request.setAttribute("categories", CATEGORIES);
                    request.getRequestDispatcher("/WEB-INF/views/service-form.jsp").forward(request, response);
                    break;

                default:   // "list"
                    request.setAttribute("services", serviceDAO.selectAll());
                    request.getRequestDispatcher("/WEB-INF/views/service-list.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("service?action=list&msg=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- POST: insert, update, delete ----------
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
                    saveService(request, response, false);
                    break;

                case "update":
                    saveService(request, response, true);
                    break;

                case "delete":
                    int id = Integer.parseInt(request.getParameter("id"));
                    try {
                        serviceDAO.delete(id);
                        response.sendRedirect("service?action=list&msg=deleted");
                    } catch (SQLIntegrityConstraintViolationException e) {
                        // This service already has bookings, so it cannot be deleted
                        response.sendRedirect("service?action=list&msg=inuse");
                    }
                    break;

                default:
                    response.sendRedirect("service?action=list");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("service?action=list&msg=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- Shared code for insert and update ----------
    private void saveService(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
            throws Exception {

        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String description = request.getParameter("description");
        String priceText = request.getParameter("price");
        String image = request.getParameter("image");

        Service s = new Service();
        s.setName(name == null ? "" : name.trim());
        s.setCategory(category);
        s.setDescription(description == null ? "" : description.trim());
        s.setImage(image == null ? "" : image.trim());
        if (isUpdate) {
            s.setId(Integer.parseInt(request.getParameter("id")));
        }

        // Server-side validation
        String error = null;
        double price = 0;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                error = "Price cannot be negative.";
            }
        } catch (Exception e) {
            error = "Please enter a valid price.";
        }
        s.setPrice(price);

        if (s.getName().isEmpty()) {
            error = "Service name is required.";
        } else if (!CATEGORIES.contains(category)) {
            error = "Please choose a valid category.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("service", s);
            request.setAttribute("categories", CATEGORIES);
            request.getRequestDispatcher("/WEB-INF/views/service-form.jsp").forward(request, response);
            return;
        }

        if (isUpdate) {
            serviceDAO.update(s);
            response.sendRedirect("service?action=list&msg=updated");
        } else {
            serviceDAO.insert(s);
            response.sendRedirect("service?action=list&msg=added");
        }
    }
}
