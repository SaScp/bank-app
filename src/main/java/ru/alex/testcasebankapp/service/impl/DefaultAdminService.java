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
import ru.alex.testcasebankapp.util.exception.SavedException;
import ru.alex.testcasebankapp.util.generator.GenerateData;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class DefaultAdminService implements AdminService {

    private final ModelMapper modelMapper;
    private UserRepository userRepository;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    public DefaultAdminService(UserRepository userRepository, JwtService jwtService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;

        this.modelMapper = modelMapper;
    }

    @Override
    public Tokens create(UserAdminDto userAdminDto, BindingResult bindingResult) {
        User user = modelMapper.map(userAdminDto, User.class);
        user.setRole("ROLE_USER");
        user.setFullName(userAdminDto.getLogin());
        user.setDataOfBirth(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (bindingResult.hasErrors()) {
            throw new SavedException(bindingResult.getFieldError().getDefaultMessage());
        }
        user.setEmails(GenerateData.generateEmailEntities(List.of(Email.builder()
                .email(userAdminDto.getEmail()).build()), user));

        user.setPhones(GenerateData.generatePhoneEntities(List.of(Phone.builder()
                .phone(userAdminDto.getPhone()).build()), user));

        user.setAccount(GenerateData.generateAccountEntity(user, userAdminDto.getInitBalance() > 0? userAdminDto.getInitBalance() : 0));

        userRepository.save(user);

        Authentication authentication =
                new PreAuthenticatedAuthenticationToken(user.getLogin(),
                        "nopassword",
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

        return jwtService.createTokens(authentication);
    }


}
