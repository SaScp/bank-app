package ru.alex.testcasebankapp.security.jwt.factory;

import org.springframework.security.core.Authentication;
import ru.alex.testcasebankapp.model.Token;

import java.util.function.Function;

public interface RefreshFactory extends Function<Authentication, Token> {
}
