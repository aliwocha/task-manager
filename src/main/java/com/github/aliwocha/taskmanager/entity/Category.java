package com.github.aliwocha.taskmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Category name is mandatory")
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Task> tasks = new ArrayList<>();

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name)
                && Objects.equals(tasks, category.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tasks);
    }

    @Override
    public String toString() {
        return "Category [" +
                "id=" + id +
                ", name=" + name +
                ']';
    }
}
