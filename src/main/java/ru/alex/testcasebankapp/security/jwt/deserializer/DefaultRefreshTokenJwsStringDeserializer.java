package ru.alex.testcasebankapp.security.jwt.deserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import ru.alex.testcasebankapp.model.entity.Token;


import java.text.ParseException;

@Slf4j
public class DefaultRefreshTokenJwsStringDeserializer implements RefreshTokenJwsStringDeserializer {
    private final JWEDecrypter jweDecrypter;

    public DefaultRefreshTokenJwsStringDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }


    @Override
    public Token apply(String token) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
            encryptedJWT.decrypt(this.jweDecrypter);

            JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();

            return new Token(claimsSet.getJWTID(), claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            log.error("token not parsed because: {}", e.getMessage());
        }
        return null;
    }

}
