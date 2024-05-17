package ru.alex.testcasebankapp.security.jwt.factory;

import ru.alex.testcasebankapp.model.Token;

import java.util.function.Function;

public interface AccessFactory extends Function<Token, Token> {
}
