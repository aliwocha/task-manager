package com.github.aliwocha.taskmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "priority", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "status", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "deadline", length = 50)
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(this.getDeadline());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title)
                && Objects.equals(description, task.description) && Objects.equals(category, task.category)
                && priority == task.priority && status == task.status && Objects.equals(deadline, task.deadline)
                && Objects.equals(user, task.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, priority, status, deadline, user);
    }

    @Override
    public String toString() {
        return "Task [" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", category=" + category.getName() +
                ", priority=" + priority +
                ", status=" + status +
                ", deadline=" + deadline +
                ", user=" + user.getLogin() +
                ']';
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED, OVERDUE
    }
}
