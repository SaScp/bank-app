package ru.alex.testcasebankapp.security.jwt.deserializer;



import java.util.function.Function;
import ru.alex.testcasebankapp.model.Token;


public interface AccessTokenJwsStringDeserializer extends Function<String, Token> {
}
