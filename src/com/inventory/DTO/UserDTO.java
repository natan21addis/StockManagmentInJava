package com.inventory.DTO;

public class UserDTO {
    private String fullName;
    private String location;
    private String phone;
    private String username;
    private String password;
    private String userType;
    private String inTime;
    private String outTime;

    public UserDTO() {
        // default constructor
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getInTime() {
        return inTime;
    }
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }
    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
