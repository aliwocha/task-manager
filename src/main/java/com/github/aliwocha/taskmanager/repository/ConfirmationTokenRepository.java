package com.github.aliwocha.taskmanager.repository;

import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByTokenIgnoreCase(String token);

    List<ConfirmationToken> findAllByUser(User user);
}
