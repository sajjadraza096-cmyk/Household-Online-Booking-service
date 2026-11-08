package com.meticulous.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.meticulous.model.Enquiry;
import com.meticulous.util.DBConnection;

public class EnquiryDAO {

    // CREATE: save a new enquiry (created_at is filled by MySQL automatically)
    public boolean insert(Enquiry e) throws Exception {
        String sql = "INSERT INTO enquiries (name, email, mobile, service_needed) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getMobile());
            ps.setString(4, e.getServiceNeeded());
            return ps.executeUpdate() > 0;
        }
    }

    // READ: get all enquiries, newest first (for the admin)
    public List<Enquiry> selectAll() throws Exception {
        List<Enquiry> list = new ArrayList<>();
        String sql = "SELECT * FROM enquiries ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // DELETE: remove an enquiry
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM enquiries WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Converts one database row into an Enquiry object
    private Enquiry mapRow(ResultSet rs) throws Exception {
        Enquiry e = new Enquiry();
        e.setId(rs.getInt("id"));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setMobile(rs.getString("mobile"));
        e.setServiceNeeded(rs.getString("service_needed"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
