package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.entity.AccountDetailsAdapter;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.repository.UserRepository;
import com.github.aliwocha.taskmanager.service.AccountDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService, UserDetailsService {

    private final UserRepository userRepository;

    public AccountDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new AccountDetailsAdapter(userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with given login does not exist")));
    }

    @Override
    public String register(User user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }

        userRepository.save(user);

        return "It works";
    }
}
