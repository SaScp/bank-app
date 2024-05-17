package ru.alex.testcasebankapp.security.authntication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.Token;
import ru.alex.testcasebankapp.repository.LogoutRepository;


import java.time.Instant;

@Component
public class TokenAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final LogoutRepository logoutRepository;

    public TokenAuthUserDetailsService(LogoutRepository logoutRepository) {
        this.logoutRepository = logoutRepository;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {

            TokenUser tokenUser = new TokenUser(token.subject(), "nopassword", true, true,
                    logoutRepository.findById(token.id()).isEmpty() &&
                            token.expireAt().isAfter(Instant.now()), true,
                    token.authorities().stream().map(SimpleGrantedAuthority::new)
                            .toList(), token);
            return tokenUser;
        }
        throw new UsernameNotFoundException("Principal must me of type Token");
    }
}
