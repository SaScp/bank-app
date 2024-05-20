package ru.alex.testcasebankapp.security.jwt.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import ru.alex.testcasebankapp.model.entity.Token;
import ru.alex.testcasebankapp.util.exception.DeserializationException;


import java.text.ParseException;

@Slf4j
public class DefaultAccessTokenJwsStringDeserializer implements AccessTokenJwsStringDeserializer {

    private final JWSVerifier jwsVerifier;


    public DefaultAccessTokenJwsStringDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public Token apply(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(this.jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(claimsSet.getJWTID(), claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException e) {

        }
        return null;
    }

}
