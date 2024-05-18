package ru.alex.testcasebankapp.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.entity.Token;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.security.jwt.factory.AccessFactory;
import ru.alex.testcasebankapp.security.jwt.factory.DefaultAccessFactory;
import ru.alex.testcasebankapp.security.jwt.factory.DefaultRefreshFactory;
import ru.alex.testcasebankapp.security.jwt.factory.RefreshFactory;
import ru.alex.testcasebankapp.security.jwt.serializer.AccessTokenJwsStringSerializer;
import ru.alex.testcasebankapp.security.jwt.serializer.RefreshTokenJweStringSerializer;
import ru.alex.testcasebankapp.service.JwtService;

@Service
public class DefaultJwtService implements JwtService {

    private final AccessFactory accessFactory = new DefaultAccessFactory();

    private final RefreshFactory refreshFactory = new DefaultRefreshFactory();

    private final AccessTokenJwsStringSerializer accessTokenJwsStringSerializer;

    private final RefreshTokenJweStringSerializer refreshTokenJweStringSerializer;

    public DefaultJwtService(AccessTokenJwsStringSerializer accessTokenJwsStringSerializer,
                             RefreshTokenJweStringSerializer refreshTokenJweStringSerializer) {
        this.accessTokenJwsStringSerializer = accessTokenJwsStringSerializer;
        this.refreshTokenJweStringSerializer = refreshTokenJweStringSerializer;
    }


    public Tokens createTokens(final Authentication authentication) {
        Token refresh = refreshFactory.apply(authentication);
        Token access = accessFactory.apply(refresh);
        return new Tokens(this.accessTokenJwsStringSerializer.apply(access),
                access.expireAt().toString(),
                this.refreshTokenJweStringSerializer.apply(refresh),
                refresh.expireAt().toString());
    }

}
