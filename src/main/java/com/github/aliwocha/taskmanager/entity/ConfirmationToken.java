package com.github.aliwocha.taskmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Token is mandatory")
    private String token;

    @NotNull(message = "CreatedAt cannot be null")
    private LocalDateTime createdAt;

    @NotNull(message = "ExpiresAt cannot be null")
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationToken that = (ConfirmationToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token)
                && Objects.equals(createdAt, that.createdAt) && Objects.equals(expiresAt, that.expiresAt)
                && Objects.equals(confirmedAt, that.confirmedAt) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiresAt, confirmedAt, user);
    }

    @Override
    public String toString() {
        return "ConfirmationToken [" +
                "id=" + id +
                ", token=" + token +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", confirmedAt=" + confirmedAt +
                ", user=" + user.getId() +
                ']';
    }
}
