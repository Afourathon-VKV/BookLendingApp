package com.Dockerates.BookLending.Entity;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StudentResp {

    private String rollNo; // Roll number of the student -> Primary Key


    private String name; // Name of the student

    private String email; // Email of the student

    private String phone; // Phone number of the student

    // No argument constructor
    public StudentResp() {
    }

    // All argument constructor
    public StudentResp(String rollNo, String name, String email, String phone) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Overridden toString() function to return in JSON {} format
    @Override
    public String toString() {
        return "Student{" +
                ", rollNo='" + rollNo + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
    // Overridden equals function : for controller level tests to mock service functionality
    // Returns true if two objects have the same value for all fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(email, student.getEmail()) &&
                Objects.equals(rollNo, student.getRollNo()) &&
                Objects.equals(name, student.getName()) &&
                Objects.equals(phone, student.getPhone());
    }

    // Used in the equals function
    // Overridden to provide a consistent hash code for objects with equal field values.
    @Override
    public int hashCode() {
        return Objects.hash(email, rollNo, name, phone);
    }
}