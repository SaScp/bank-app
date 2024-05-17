package ru.alex.testcasebankapp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.UserService;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository,
                              ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("user not found!"));
    }

    @Override
    public Map<String, String> save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        user.setPhones(generatePhoneEntities(user.getPhones(), user));
        user.setEmails(generateEmailEntities(user.getEmails(), user));
        user.setAccount(generateAccountEntity(user));

        userRepository.save(user);

        return Map.of(
                "login", user.getLogin(),
                "role", user.getRole()
        );
    }

    private Set<Email> generateEmailEntities(Set<Email> emails, User user) {
        return emails.stream().map(email -> {
            email.setUser(user);
            return email;
        }).collect(Collectors.toSet());
    }

    private Set<Phone> generatePhoneEntities(Set<Phone> phones, User user) {
        return phones.stream().map(email -> {
            email.setUser(user);
            return email;
        }).collect(Collectors.toSet());
    }

    private Account generateAccountEntity(User user) {
        return Account.builder()
                .card(generateNumberCard())
                .user(user)
                .currentBalance(0)
                .initialDeposit(0)
                .build();
    }

    private String generateNumberCard() {
        return "";
    }
}
