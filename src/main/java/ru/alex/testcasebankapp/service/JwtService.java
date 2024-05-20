package ru.alex.testcasebankapp.service;

import org.springframework.security.core.Authentication;
import ru.alex.testcasebankapp.model.response.Tokens;


public interface JwtService {
    Tokens createTokens(final Authentication authentication);
}
