package com.meticulous.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.meticulous.model.Service;
import com.meticulous.util.DBConnection;

public class ServiceDAO {

    // CREATE: add a new service
    public boolean insert(Service s) throws Exception {
        String sql = "INSERT INTO services (name, category, description, price, image) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getCategory());
            ps.setString(3, s.getDescription());
            ps.setDouble(4, s.getPrice());
            ps.setString(5, s.getImage());
            return ps.executeUpdate() > 0;
        }
    }

    // READ: get all services
    public List<Service> selectAll() throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    // READ: get services of one category (CLEANING / HANDYMAN / GARDEN)
    public List<Service> selectByCategory(String category) throws Exception {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE category = ? ORDER BY id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    // READ: get one service by id (returns null if not found)
    public Service selectById(int id) throws Exception {
        String sql = "SELECT * FROM services WHERE id = ?";
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

    // UPDATE: change an existing service
    public boolean update(Service s) throws Exception {
        String sql = "UPDATE services SET name = ?, category = ?, description = ?, price = ?, image = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getCategory());
            ps.setString(3, s.getDescription());
            ps.setDouble(4, s.getPrice());
            ps.setString(5, s.getImage());
            ps.setInt(6, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: remove a service
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM services WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Converts one database row into a Service object
    private Service mapRow(ResultSet rs) throws Exception {
        Service s = new Service();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setCategory(rs.getString("category"));
        s.setDescription(rs.getString("description"));
        s.setPrice(rs.getDouble("price"));
        s.setImage(rs.getString("image"));
        return s;
    }
}
