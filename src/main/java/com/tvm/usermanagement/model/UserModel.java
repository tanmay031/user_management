package com.tvm.usermanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Specify generation strategy
    private Long id;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotEmpty(message = "Email is required")
    private String email;

    // Default constructor
    public UserModel() {
    }

    // Parameterized constructor
    public UserModel(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
