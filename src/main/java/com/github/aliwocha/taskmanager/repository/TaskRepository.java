package com.github.aliwocha.taskmanager.repository;

import com.github.aliwocha.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByCategory_Id(Long categoryId);
}
