package ru.alex.testcasebankapp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.AdminService;
import ru.alex.testcasebankapp.service.JwtService;
import ru.alex.testcasebankapp.util.exception.LoginException;
import ru.alex.testcasebankapp.util.generator.GenerateData;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class DefaultAdminService implements AdminService {

    private UserRepository userRepository;

    private JwtService jwtService;

    private List<Validator> validators;

    public DefaultAdminService(UserRepository userRepository, JwtService jwtService, List<Validator> validators) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.validators = validators;
    }

    @Override
    public Tokens create(UserAdminDto userAdminDto, BindingResult bindingResult) {
        User user = User.builder()
                .login(userAdminDto.getLogin())
                .role("ROLE_USER")
                .fullName(userAdminDto.getLogin())
                .password(userAdminDto.getPassword())
                .dataOfBirth(LocalDateTime.now())
                .build();

        for (var i : validators) {
            i.validate(user, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            throw new LoginException(bindingResult.getFieldError().getDefaultMessage());
        }
        user.setEmails(GenerateData.generateEmailEntities(Set.of(Email.builder()
                .email(userAdminDto.getEmail()).build()), user));

        user.setPhones(GenerateData.generatePhoneEntities(Set.of(Phone.builder()
                .phone(userAdminDto.getPhone()).build()), user));

        user.setAccount(GenerateData.generateAccountEntity(user, userAdminDto.getInitBalance()));

        userRepository.save(user);

        Authentication authentication =
                new PreAuthenticatedAuthenticationToken(user.getLogin(),
                        "nopassword",
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

        return jwtService.createTokens(authentication);
    }


}
