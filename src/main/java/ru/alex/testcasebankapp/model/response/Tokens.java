package ru.alex.testcasebankapp.model.response;

public record Tokens(String accessToken, String accessTokenExp, String refreshToken, String refreshTokenExp) {
}
