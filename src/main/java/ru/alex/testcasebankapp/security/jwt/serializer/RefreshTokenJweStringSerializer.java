package ru.alex.testcasebankapp.security.jwt.serializer;


import ru.alex.testcasebankapp.model.entity.Token;

import java.util.function.Function;

public interface RefreshTokenJweStringSerializer extends Function<Token, String> {
}
