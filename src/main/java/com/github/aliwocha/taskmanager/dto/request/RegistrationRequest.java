package com.github.aliwocha.taskmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;

public class RegistrationRequest {

    /**
     * Allowing digits, lowercase and uppercase letters, and special characters ("_","-").
     * At least 4 characters in length, but no more than 50.
     */
    private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_-]{4,50}$";

    /**
     * At least one digit.
     * At least one lowercase letter.
     * At least one uppercase letter.
     * At least 8 characters in length, but no more than 50.
     */
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,50}$";

    @NotBlank(message = "Login is mandatory")
    @Size(min = 4, max = 50, message = "Login must have between 4 and 50 characters")
    @Pattern(regexp = LOGIN_PATTERN, message = "Invalid login format")
    private String login;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 50, message = "Password must have between 8 and 50 characters")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Invalid password format")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 100, message = "Email must have max 100 characters")
    private String email;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Timestamp registrationDate = Timestamp.from(Instant.now()); // UTC time zone

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }
}
