package com.github.aliwocha.taskmanager.repository;

import com.github.aliwocha.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByCategory_Id(Long categoryId);

    Page<Task> findAllByUser_Id(Long userId, Pageable pageable);

    Page<Task> findAllByStatus(Task.Status status, Pageable pageable);

    Page<Task> findAllByStatusAndUser_Id(Task.Status status, Long userId, Pageable pageable);

    Optional<Task> findByIdAndUser_Id(Long taskId, Long userId);
}
