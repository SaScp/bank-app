package ru.alex.testcasebankapp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.entity.SearchEntity;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.util.generator.GenerateData;


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

        user.setPhones(GenerateData.generatePhoneEntities(user.getPhones(), user));
        user.setEmails(GenerateData.generateEmailEntities(user.getEmails(), user));
        user.setAccount(GenerateData.generateAccountEntity(user, 50));

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
                                GenerateData.generatePageRequest(paginationEntity))
                        .orElseThrow(() -> new RuntimeException());
            }
            case DATE -> {
                return userRepository.findAllByDataOfBirthGreaterThan(searchEntity.getDate(),
                                GenerateData.generatePageRequest(paginationEntity))
                        .orElseThrow(() -> new RuntimeException());
            }
            default -> throw new IllegalStateException("Unexpected value: " + searchEntity.getType());
        }
    }


}
