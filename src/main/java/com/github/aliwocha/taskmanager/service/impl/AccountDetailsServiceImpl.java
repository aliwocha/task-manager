package com.github.aliwocha.taskmanager.service.impl;

import com.github.aliwocha.taskmanager.dto.response.ConfirmationTokenResponse;
import com.github.aliwocha.taskmanager.entity.AccountDetailsAdapter;
import com.github.aliwocha.taskmanager.entity.ConfirmationToken;
import com.github.aliwocha.taskmanager.entity.User;
import com.github.aliwocha.taskmanager.exception.email.DuplicateEmailException;
import com.github.aliwocha.taskmanager.exception.email.EmailAlreadyConfirmedException;
import com.github.aliwocha.taskmanager.exception.email.InvalidEmailException;
import com.github.aliwocha.taskmanager.exception.token.TokenNotExpiredException;
import com.github.aliwocha.taskmanager.exception.user.DuplicateUserException;
import com.github.aliwocha.taskmanager.exception.user.UserNotFoundException;
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

    public AccountDetailsServiceImpl(UserRepository userRepository, ConfirmationTokenService confirmationTokenService,
                                     EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new AccountDetailsAdapter(userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with given login not found")));
    }

    @Transactional
    @Override
    public ConfirmationTokenResponse registerUser(User user) {
        checkIfUserNotDuplicated(user);
        checkIfEmailNotDuplicated(user);

        userRepository.save(user);
        return sendConfirmationEmail(user);
    }

    private void checkIfUserNotDuplicated(User user) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(user.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException();
        }
    }

    private void checkIfEmailNotDuplicated(User user) {
        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    @Override
    public void enableUser(User user) {
        userRepository.findByEmailIgnoreCase(user.getEmail()).ifPresent(u -> u.setEnabled(true));
    }

    @Override
    public ConfirmationTokenResponse resendConfirmationEmail(String login, String email) {
        Optional<User> userByLogin = userRepository.findByLoginIgnoreCase(login);
        if (userByLogin.isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = userByLogin.get();
        verifyUser(email, user);

        return sendConfirmationEmail(user);
    }

    private void verifyUser(String email, User user) {
        if (!email.equals(user.getEmail())) {
            throw new InvalidEmailException("Invalid email for given login");
        } else if (user.getEnabled()) {
            throw new EmailAlreadyConfirmedException();
        } else if (!confirmationTokenService.checkIfAllTokensExpired(user)) {
            throw new TokenNotExpiredException();
        }
    }

    private ConfirmationTokenResponse sendConfirmationEmail(User user) {
        ConfirmationToken confirmationToken = confirmationTokenService.createToken(user);
        confirmationTokenService.saveToken(confirmationToken);

        String tokenValue = confirmationToken.getToken();
        String url = "http://localhost:8080/api/register/confirm?token=" + tokenValue;

        emailService.sendEmail(user.getEmail(), "Confirm your email",
                "Confirm registration by clicking in the link: " + url, false);

        return ConfirmationTokenResponse.of(tokenValue);
    }
}
