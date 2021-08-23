package com.prime.rush_hour.dtos;

import com.prime.rush_hour.security.authorization.ApplicationUserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserPutDto {


    private String firstName;
    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String password;
    private List<ApplicationUserRole> roles;

    public UserPutDto() {
    }

    public UserPutDto(String firstName, String lastName, String email, String password, List<ApplicationUserRole> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ApplicationUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<ApplicationUserRole> roles) {
        this.roles = roles;
    }
}
