package ru.alex.testcasebankapp.security.jwt.deserializer;
import ru.alex.testcasebankapp.model.Token;



import java.util.function.Function;

public interface RefreshTokenJwsStringDeserializer extends Function<String, Token> {
}
