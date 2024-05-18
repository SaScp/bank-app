package ru.alex.testcasebankapp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.PaginationEntity;
import ru.alex.testcasebankapp.model.SearchEntity;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.UserService;


import java.time.LocalDateTime;
import java.util.*;
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
    public User findById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
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

    @Override
    public List<User> searchClient(SearchEntity searchEntity, PaginationEntity paginationEntity) {


        switch (searchEntity.getType()) {
            case EMAIL -> {
                return List.of(userRepository.findByEmailsIn(searchEntity.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found")));
            }
            case PHONE -> {
                return List.of(userRepository.findByPhone(searchEntity.getPhone())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found")));
            }
            case FULLNAME -> {
                return userRepository.findAllByFullNameIsLike(searchEntity.getFullName(),
                                generatePageRequest(paginationEntity))
                        .orElseThrow(() -> new RuntimeException());
            }
            case DATE -> {
                return userRepository.findAllByDataOfBirthGreaterThan(searchEntity.getDate(),
                                generatePageRequest(paginationEntity))
                        .orElseThrow(() -> new RuntimeException());
            }
            default -> throw new IllegalStateException("Unexpected value: " + searchEntity.getType());
        }
    }

    private PageRequest generatePageRequest(PaginationEntity paginationEntity) {
        if (paginationEntity.getPropertySort().isEmpty()) {
            return PageRequest.of(
                    paginationEntity.getPageNumber(),
                    paginationEntity.getPageSize()
            );
        } else {
            return PageRequest.of(
                    paginationEntity.getPageNumber(),
                    paginationEntity.getPageSize(),
                    Sort.by(paginationEntity.getPropertySort())
            );
        }
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
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(new Random().nextInt());
        }
        return builder.toString();
    }
}
