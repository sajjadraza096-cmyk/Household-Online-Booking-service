package com.meticulous.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.meticulous.model.User;
import com.meticulous.util.DBConnection;

public class UserDAO {

    // CREATE: register a new user (password is stored as a hash, never plain text)
    public boolean register(User user) throws Exception {
        String sql = "INSERT INTO users (name, email, mobile, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getMobile());
            ps.setString(4, hashPassword(user.getPassword()));
            ps.setString(5, user.getRole());
            return ps.executeUpdate() > 0;
        }
    }

    // READ: login check. Returns the User if email and password match, otherwise null
    public User login(String email, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hashPassword(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // READ: check whether an email is already registered
    public boolean emailExists(String email) throws Exception {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Converts one database row into a User object
    private User mapRow(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setMobile(rs.getString("mobile"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    }

    // Hashes the password using SHA-256
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
