package com.github.aliwocha.taskmanager.repository;

import com.github.aliwocha.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIgnoreCase(String login);

    Optional<User> findByEmailIgnoreCase(String email);
}
