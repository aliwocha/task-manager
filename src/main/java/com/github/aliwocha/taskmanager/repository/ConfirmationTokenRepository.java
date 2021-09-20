package com.github.aliwocha.taskmanager.repository;

import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByTokenIgnoreCase(String token);
}
