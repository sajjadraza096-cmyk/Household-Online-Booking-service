package com.meticulous.model;

import java.sql.Timestamp;

public class Enquiry {

    private int id;
    private String name;
    private String email;
    private String mobile;
    private String serviceNeeded;
    private Timestamp createdAt;

    // Empty constructor
    public Enquiry() {
    }

    // Constructor without id and date (used when a visitor sends a new enquiry)
    public Enquiry(String name, String email, String mobile, String serviceNeeded) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.serviceNeeded = serviceNeeded;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getServiceNeeded() {
        return serviceNeeded;
    }

    public void setServiceNeeded(String serviceNeeded) {
        this.serviceNeeded = serviceNeeded;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
