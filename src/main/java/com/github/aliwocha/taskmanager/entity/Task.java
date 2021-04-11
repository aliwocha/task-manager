package com.github.aliwocha.taskmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotNull(message = "Description cannot be null")
    @Column(length = 1024)
    private String description;
    @NotNull(message = "Category cannot be null")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate deadline;

    public Task() {
    }

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
                && priority == task.priority && status == task.status && Objects.equals(deadline, task.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, priority, status, deadline);
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
                ']';
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED, OVERDUE
    }
}
