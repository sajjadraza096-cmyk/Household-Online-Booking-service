package com.meticulous.model;

import java.sql.Date;

public class Booking {

    private int id;
    private int userId;
    private int serviceId;
    private Date bookingDate;
    private String address;
    private String status;

    // Extra fields (filled from JOIN queries, not stored in the bookings table)
    private String userName;
    private String serviceName;

    // Empty constructor
    public Booking() {
    }

    // Constructor without id (used when creating a new booking)
    public Booking(int userId, int serviceId, Date bookingDate, String address) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
        this.address = address;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
