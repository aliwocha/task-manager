package com.github.aliwocha.taskmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Login is mandatory")
    @Column(unique = true)
    private String login;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 3, max = 100, message = "Password must have between 3 and 100 characters")
    private String password;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(name = "enabled")
    private Boolean isEnabled;

    @NotNull(message = "Role cannot be null")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login)
                && Objects.equals(password, user.password) && Objects.equals(email, user.email)
                && Objects.equals(isEnabled, user.isEnabled) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, email, isEnabled, role);
    }

    @Override
    public String toString() {
        return "User [" +
                "id=" + id +
                ", login=" + login +
                ", password=" + password +
                ", email=" + email +
                ", isEnabled=" + isEnabled +
                ", role=" + role.getName() +
                ']';
    }
}
