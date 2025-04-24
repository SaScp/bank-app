package ru.alex.testcasebankapp.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.response.Tokens;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.EmailRepository;
import ru.alex.testcasebankapp.repository.PhoneRepository;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.AdminService;
import ru.alex.testcasebankapp.service.JwtService;
import ru.alex.testcasebankapp.util.exception.SavedException;
import ru.alex.testcasebankapp.util.generator.GenerateData;
import ru.alex.testcasebankapp.util.mapper.UserMapper;
import ru.alex.testcasebankapp.util.validator.AuthValidator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.alex.testcasebankapp.util.Constant.DEFAULT_BALANCE;

@Slf4j
@Service
public class DefaultAdminService implements AdminService {


    private UserRepository userRepository;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    private EmailRepository emailRepository;

    private PhoneRepository phoneRepository;

    private List<AuthValidator> validators;

    public DefaultAdminService(UserRepository userRepository,
                               JwtService jwtService,
                               PasswordEncoder passwordEncoder,
                               EmailRepository emailRepository,
                               PhoneRepository phoneRepository,
                               List<AuthValidator> validators) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
        this.validators = validators;
    }

    @Override
    public Tokens create(UserAdminDto userAdminDto, BindingResult bindingResult) {

        ru.alex.testcasebankapp.model.dto.UserDto userDto = ru.alex.testcasebankapp.model.dto.UserDto.builder()
                .login(userAdminDto.getLogin())
                .password(userAdminDto.getPassword())
                .build();
        validate(userDto, userAdminDto, bindingResult);

        User user = generate(userAdminDto);

        userRepository.save(user);

        Authentication authentication =
                new PreAuthenticatedAuthenticationToken(user.getLogin(),
                        "nopassword",
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

        return jwtService.createTokens(authentication);
    }

    private User generate(UserAdminDto userAdminDto) {
        User userDto = UserMapper.INSTANCE.fromAdminDto(userAdminDto);
        userDto.setRole("ROLE_USER");
        userDto.setFullName(userAdminDto.getLogin());
        userDto.setDataOfBirth(LocalDateTime.now());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));


        userDto.setEmails(GenerateData.generateEmailEntities(Set.of(Email.builder()
                .email(userAdminDto.getEmail()).build()), userDto));

        userDto.setPhones(GenerateData.generatePhoneEntities(Set.of(Phone.builder()
                .phone(userAdminDto.getPhone()).build()), userDto));

        userDto.setAccounts(GenerateData.generateAccountEntity(userDto, userAdminDto.getInitBalance() > 0? userAdminDto.getInitBalance() : DEFAULT_BALANCE));
        return userDto;
    }


    private void validate(ru.alex.testcasebankapp.model.dto.UserDto userDto, UserAdminDto userAdminDto, BindingResult bindingResult) {
        for (var i : validators) {
            i.validate(userDto, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            String string = Optional.ofNullable(bindingResult.getFieldError()).isPresent() ? bindingResult.getFieldError().getDefaultMessage() : "none";
            log.error("::SavedException:: \"user not saved because: {}\"", string);
            throw new SavedException(string);
        }
        if (findByPhone(userAdminDto.getPhone()).isPresent() || findByEmail(userAdminDto.getEmail()).isPresent()) {
            log.error("::SavedException:: \"user not saved because: phone or email already exists\"");
            throw new SavedException("phone or email already exists");
        }
    }

    private Optional<Email> findByEmail(String email) {
        return emailRepository.findByEmail(email);
    }

    private Optional<Phone> findByPhone(String phone) {
        return phoneRepository.findByPhone(phone);
    }

}
