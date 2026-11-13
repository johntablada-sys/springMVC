package com.restapi.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

public class StudentDto {

    public interface OnCreate extends Default {
    }

    private Long id;

    @NotBlank(groups = OnCreate.class)
    @Size(min = 2, max = 50, message = "Must be 2 to 50 characters.")
    private String firstName;

    @NotBlank(groups = OnCreate.class)
    @Size(min = 2, max = 50, message = "Must be 2 to 50 characters.")
    private String lastName;

    @NotBlank(message = "Email is required and should be valid", groups = OnCreate.class)
    @Email(message = "Email is required and should be valid")
    private String email;

    public StudentDto() {
    }

    public StudentDto(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
