package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.AccountDetailsAdapter;
import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.DuplicateEmailException;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.repository.UserRepository;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import com.github.aliwocha.taskmanager.service.ConfirmationTokenService;
import com.github.aliwocha.taskmanager.service.EmailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService, UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public AccountDetailsServiceImpl(UserRepository userRepository, ConfirmationTokenService confirmationTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new AccountDetailsAdapter(userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with given login does not exist")));
    }

    @Transactional
    @Override
    public void registerUser(User user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }

        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new DuplicateEmailException();
        }

        userRepository.save(user);
        sendToken(user);
    }

    @Override
    public void enableUser(User user) {
        userRepository.findByEmailIgnoreCase(user.getEmail()).ifPresent(u -> u.setEnabled(true));
    }

    private void sendToken(User user) {
        ConfirmationToken confirmationToken = confirmationTokenService.createToken(user);
        confirmationTokenService.saveToken(confirmationToken);

        String url = "http://localhost:8080/api/register/confirm?token=" + confirmationToken.getToken();

        emailService.sendEmail(user.getEmail(), "Confirm your email",
                "Confirm registration by clicking in the link " + url, false);
    }
}
