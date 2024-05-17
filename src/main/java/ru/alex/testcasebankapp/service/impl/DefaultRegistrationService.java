package ru.alex.testcasebankapp.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.service.JwtService;
import ru.alex.testcasebankapp.service.RegistrationService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.util.exception.RegistrationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultRegistrationService implements RegistrationService {

    private final UserService userService;

    private final List<Validator> validators;

    private final JwtService jwtService;


    public DefaultRegistrationService(UserService userService,
                                      List<Validator> validators,
                                      JwtService jwtService) {
        this.userService = userService;
        this.validators = validators;
        this.jwtService = jwtService;
    }

    @Override
    public Tokens registration(UserDto userDto, BindingResult bindingResult) {

        for (var i : validators) {
            i.validate(userDto, bindingResult);
            if (bindingResult.hasErrors()) {
                throw new RegistrationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }

        Map<String, String> authenticationData = this.userService.save(userDto);

        Authentication authentication =
                new PreAuthenticatedAuthenticationToken(authenticationData.get("login"),
                        "nopassword",
                        Collections.singleton(new SimpleGrantedAuthority(authenticationData.get("role"))));


        return jwtService.createTokens(authentication);
    }

}
