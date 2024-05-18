package ru.alex.testcasebankapp.security.jwt.deserializer;



import java.util.function.Function;
import ru.alex.testcasebankapp.model.entity.Token;


public interface AccessTokenJwsStringDeserializer extends Function<String, Token> {
}
