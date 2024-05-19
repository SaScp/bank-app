package ru.alex.testcasebankapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.JwtService;
import ru.alex.testcasebankapp.service.LoginService;
import ru.alex.testcasebankapp.util.exception.LoginException;

@Slf4j
@Service
public class DefaultLoginService implements LoginService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public DefaultLoginService(JwtService jwtService,
                               AuthenticationManager authenticationManager){
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Tokens login(UserDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("::LoginException::\"user not login because: {} \"", bindingResult.getFieldError().getDefaultMessage());
            throw new LoginException(bindingResult.getFieldError().getDefaultMessage());
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDto.getLogin(),
                        userDto.getPassword());

        return jwtService.createTokens(authenticationManager.authenticate(authentication));
    }
}
