package com.meticulous.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.meticulous.model.Booking;
import com.meticulous.util.DBConnection;

public class BookingDAO {

    // Base query: joins bookings with users and services to get the names
    private static final String BASE_SELECT =
        "SELECT b.*, u.name AS user_name, s.name AS service_name " +
        "FROM bookings b " +
        "JOIN users u ON b.user_id = u.id " +
        "JOIN services s ON b.service_id = s.id ";

    // CREATE: add a new booking
    public boolean insert(Booking b) throws Exception {
        String sql = "INSERT INTO bookings (user_id, service_id, booking_date, address, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getUserId());
            ps.setInt(2, b.getServiceId());
            ps.setDate(3, b.getBookingDate());
            ps.setString(4, b.getAddress());
            ps.setString(5, b.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    // READ: all bookings of one customer
    public List<Booking> selectByUser(int userId) throws Exception {
        List<Booking> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE b.user_id = ? ORDER BY b.booking_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // READ: all bookings of all customers (for admin)
    public List<Booking> selectAll() throws Exception {
        List<Booking> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY b.booking_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // READ: one booking by id (returns null if not found)
    public Booking selectById(int id) throws Exception {
        String sql = BASE_SELECT + "WHERE b.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // UPDATE: change service, date and address of a booking
    public boolean update(Booking b) throws Exception {
        String sql = "UPDATE bookings SET service_id = ?, booking_date = ?, address = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getServiceId());
            ps.setDate(2, b.getBookingDate());
            ps.setString(3, b.getAddress());
            ps.setInt(4, b.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // UPDATE: change only the status (used by admin: CONFIRMED / COMPLETED / CANCELLED)
    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: remove a booking
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Converts one database row into a Booking object
    private Booking mapRow(ResultSet rs) throws Exception {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setUserId(rs.getInt("user_id"));
        b.setServiceId(rs.getInt("service_id"));
        b.setBookingDate(rs.getDate("booking_date"));
        b.setAddress(rs.getString("address"));
        b.setStatus(rs.getString("status"));
        b.setUserName(rs.getString("user_name"));
        b.setServiceName(rs.getString("service_name"));
        return b;
    }
}
