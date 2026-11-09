package com.meticulous.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.meticulous.dao.BookingDAO;
import com.meticulous.dao.ServiceDAO;
import com.meticulous.model.Booking;
import com.meticulous.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<String> STATUSES =
            Arrays.asList("PENDING", "CONFIRMED", "COMPLETED", "CANCELLED");

    private BookingDAO bookingDAO = new BookingDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();

    // ---------- Helpers ----------
    private User getLoggedUser(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return null;
        }
        return (User) request.getSession(false).getAttribute("user");
    }

    private boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    // Admin can modify any booking. A customer can modify only their own PENDING bookings.
    private boolean canModify(User user, Booking booking) {
        if (isAdmin(user)) {
            return true;
        }
        return booking.getUserId() == user.getId() && "PENDING".equals(booking.getStatus());
    }

    // ---------- Login check (runs before every request) ----------
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (getLoggedUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login?msg=loginrequired");
            return;
        }
        super.service(request, response);   // continue to doGet / doPost
    }

    // ---------- GET: list, new, edit ----------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedUser(request);
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {

                case "new": {
                    Booking b = new Booking();
                    String sid = request.getParameter("serviceId");   // sent by the "Book Now" button
                    if (sid != null && sid.matches("\\d+")) {
                        b.setServiceId(Integer.parseInt(sid));
                    }
                    showForm(request, response, b);
                    break;
                }

                case "edit": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Booking b = bookingDAO.selectById(id);
                    if (b == null || !canModify(user, b)) {
                        response.sendRedirect("booking?action=list&msg=notallowed");
                        return;
                    }
                    showForm(request, response, b);
                    break;
                }

                default: {   // "list"
                    List<Booking> bookings = isAdmin(user)
                            ? bookingDAO.selectAll()
                            : bookingDAO.selectByUser(user.getId());
                    request.setAttribute("bookings", bookings);
                    request.setAttribute("statuses", STATUSES);
                    request.getRequestDispatcher("/WEB-INF/views/booking-list.jsp").forward(request, response);
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("booking?action=list&msg=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- POST: insert, update, delete, status ----------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        User user = getLoggedUser(request);
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {

                case "insert":
                    saveBooking(request, response, user, false);
                    break;

                case "update":
                    saveBooking(request, response, user, true);
                    break;

                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Booking b = bookingDAO.selectById(id);
                    if (b == null || !canModify(user, b)) {
                        response.sendRedirect("booking?action=list&msg=notallowed");
                        return;
                    }
                    bookingDAO.delete(id);
                    response.sendRedirect("booking?action=list&msg=deleted");
                    break;
                }

                case "status": {   // admin only
                    if (!isAdmin(user)) {
                        response.sendRedirect("booking?action=list&msg=notallowed");
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    String status = request.getParameter("status");
                    if (!STATUSES.contains(status)) {
                        response.sendRedirect("booking?action=list&msg=invalid");
                        return;
                    }
                    bookingDAO.updateStatus(id, status);
                    response.sendRedirect("booking?action=list&msg=statusupdated");
                    break;
                }

                default:
                    response.sendRedirect("booking?action=list");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("booking?action=list&msg=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // ---------- Shows the booking form (used for new, edit and validation errors) ----------
    private void showForm(HttpServletRequest request, HttpServletResponse response, Booking booking)
            throws Exception {
        request.setAttribute("booking", booking);
        request.setAttribute("services", serviceDAO.selectAll());
        request.setAttribute("minDate", LocalDate.now().toString());   // earliest allowed date
        request.getRequestDispatcher("/WEB-INF/views/booking-form.jsp").forward(request, response);
    }

    // ---------- Shared code for insert and update ----------
    private void saveBooking(HttpServletRequest request, HttpServletResponse response,
                             User user, boolean isUpdate) throws Exception {

        Booking b = new Booking();

        if (isUpdate) {
            int id = Integer.parseInt(request.getParameter("id"));
            Booking existing = bookingDAO.selectById(id);
            // Check ownership against the DATABASE record, never trust the form
            if (existing == null || !canModify(user, existing)) {
                response.sendRedirect("booking?action=list&msg=notallowed");
                return;
            }
            b.setId(id);
            b.setUserId(existing.getUserId());
            b.setStatus(existing.getStatus());
        } else {
            b.setUserId(user.getId());
            b.setStatus("PENDING");
        }

        String serviceIdText = request.getParameter("serviceId");
        String dateText = request.getParameter("bookingDate");
        String address = request.getParameter("address");
        b.setAddress(address == null ? "" : address.trim());

        // ----- Server-side validation -----
        String error = null;

        try {
            b.setServiceId(Integer.parseInt(serviceIdText));
            if (serviceDAO.selectById(b.getServiceId()) == null) {
                error = "Please choose a valid service.";
            }
        } catch (Exception e) {
            error = "Please choose a service.";
        }

        if (error == null) {
            try {
                Date date = Date.valueOf(dateText);       // expects yyyy-MM-dd
                b.setBookingDate(date);
                if (date.toLocalDate().isBefore(LocalDate.now())) {
                    error = "Booking date cannot be in the past.";
                }
            } catch (Exception e) {
                error = "Please choose a valid booking date.";
            }
        }

        if (error == null) {
            if (b.getAddress().isEmpty()) {
                error = "Address is required.";
            } else if (b.getAddress().length() > 255) {
                error = "Address is too long (maximum 255 characters).";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            showForm(request, response, b);     // back to the form, keeping what the user typed
            return;
        }

        // ----- Save -----
        if (isUpdate) {
            bookingDAO.update(b);
            response.sendRedirect("booking?action=list&msg=updated");
        } else {
            bookingDAO.insert(b);
            response.sendRedirect("booking?action=list&msg=added");
        }
    }
}
