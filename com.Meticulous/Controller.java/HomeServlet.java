package com.meticulous.controller;

import java.io.IOException;
import java.util.List;

import com.meticulous.dao.ServiceDAO;
import com.meticulous.model.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// "" = the project root URL, "/home" = the home URL
@WebServlet({"", "/home"})
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Load the services of each category from the database
            List<Service> cleaning = serviceDAO.selectByCategory("CLEANING");
            List<Service> handyman = serviceDAO.selectByCategory("HANDYMAN");
            List<Service> garden   = serviceDAO.selectByCategory("GARDEN");

            request.setAttribute("cleaningServices", cleaning);
            request.setAttribute("handymanServices", handyman);
            request.setAttribute("gardenServices", garden);

            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Could not load services", e);
        }
    }
}
