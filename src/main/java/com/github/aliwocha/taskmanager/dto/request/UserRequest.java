package com.github.aliwocha.taskmanager.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Login is mandatory")
    @Size(min = 4, max = 100, message = "Login must have between 4 and 100 characters")
    private String login;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 100, message = "Password must have between 8 and 100 characters")
    private String password;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 100, message = "Email must have max 100 characters")
    private String email;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 50, message = "Role must have max 50 characters")
    private String role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
