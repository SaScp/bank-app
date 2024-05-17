package ru.alex.testcasebankapp.security.authntication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.service.UserService;


import java.util.Collections;


@Component
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        final var user = userService.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return DefaultUserDetails
                .builder()
                .user(user)
                .authorities(Collections
                        .singleton(new SimpleGrantedAuthority(user.getRole())))
                .build();
    }
}
