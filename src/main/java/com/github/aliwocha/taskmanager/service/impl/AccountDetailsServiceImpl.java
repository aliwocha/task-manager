package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.AccountDetailsAdapter;
import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.DuplicateEmailException;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.repository.UserRepository;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import com.github.aliwocha.taskmanager.service.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService, UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenService tokenService;

    public AccountDetailsServiceImpl(UserRepository userRepository, ConfirmationTokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new AccountDetailsAdapter(userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with given login does not exist")));
    }

    @Override
    public String registerUser(User user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }

        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new DuplicateEmailException();
        }

        userRepository.save(user);

        ConfirmationToken confirmationToken = tokenService.createToken(user);
        tokenService.saveToken(confirmationToken);

        return confirmationToken.getToken();
    }

    @Override
    public void enableUser(User user) {
        userRepository.findByEmailIgnoreCase(user.getEmail()).ifPresent(u -> u.setEnabled(true));
    }
}
