package com.github.aliwocha.taskmanager.dto.response;

public class ConfirmationTokenResponse {

    private final String token;

    public ConfirmationTokenResponse(String tokenValue) {
        this.token = tokenValue;
    }

    public String getToken() {
        return token;
    }

    public static ConfirmationTokenResponse of(String tokenValue) {
        return new ConfirmationTokenResponse(tokenValue);
    }
}
