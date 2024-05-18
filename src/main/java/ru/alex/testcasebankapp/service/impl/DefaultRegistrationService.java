package ru.alex.testcasebankapp.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.JwtService;
import ru.alex.testcasebankapp.service.RegistrationService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.util.exception.RegistrationException;
import ru.alex.testcasebankapp.util.validator.impl.UserValidator;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultRegistrationService implements RegistrationService {

    private final UserService userService;

    private final UserValidator validators;

    private final JwtService jwtService;


    public DefaultRegistrationService(UserService userService,
                                      UserValidator validators,
                                      JwtService jwtService) {
        this.userService = userService;
        this.validators = validators;
        this.jwtService = jwtService;
    }

    @Override
    public Tokens registration(UserDto userDto, BindingResult bindingResult) {
        validators.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new RegistrationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        Map<String, String> authenticationData = this.userService.save(userDto, bindingResult);

        Authentication authentication =
                new PreAuthenticatedAuthenticationToken(authenticationData.get("login"),
                        "nopassword",
                        Collections.singleton(new SimpleGrantedAuthority(authenticationData.get("role"))));


        return jwtService.createTokens(authentication);
    }

}
