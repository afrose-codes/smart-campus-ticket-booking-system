package com.example.campusevent.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be at least 3 characters")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be at least 4 characters")
    @Pattern(regexp = "^\\S+$", message = "Username must not contain spaces")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    private String role = "STUDENT";

    public RegisterDTO() {}

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getRole() { return role; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setRole(String role) { this.role = role; }
}
