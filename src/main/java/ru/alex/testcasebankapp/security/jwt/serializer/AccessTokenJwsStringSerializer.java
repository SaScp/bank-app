package ru.alex.testcasebankapp.security.jwt.serializer;
import ru.alex.testcasebankapp.model.Token;


import java.util.function.Function;

public interface AccessTokenJwsStringSerializer extends Function<Token, String> {
}
