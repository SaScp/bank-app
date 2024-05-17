package ru.alex.testcasebankapp.security.jwt.factory;

import lombok.Setter;


import java.time.Duration;
import java.time.Instant;
import ru.alex.testcasebankapp.model.Token;


@Setter
public class DefaultAccessFactory implements AccessFactory {

    private Duration duration = Duration.ofMinutes(5);
    @Override
    public Token apply(Token token) {
        var now = Instant.now();
        return new Token(token.id(), token.subject(), token.authorities().stream()
                .filter(auth -> auth.startsWith("GRAND_"))
                .map(auth -> auth.replace("GRAND_", "")).toList(),now, now.plus(duration));
    }

}
