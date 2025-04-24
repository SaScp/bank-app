package ru.alex.testcasebankapp.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.entity.SearchEntity;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.UserRepository;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.service.add.AddComponent;
import ru.alex.testcasebankapp.service.delete.DeleteComponent;
import ru.alex.testcasebankapp.service.update.UpdateComponent;
import ru.alex.testcasebankapp.util.exception.SavedException;
import ru.alex.testcasebankapp.util.exception.TransactionNotFoundException;
import ru.alex.testcasebankapp.util.exception.UpdateException;
import ru.alex.testcasebankapp.util.generator.GenerateData;
import ru.alex.testcasebankapp.util.mapper.UserMapper;
import ru.alex.testcasebankapp.util.validator.DataValidator;


import java.time.LocalDateTime;
import java.util.*;

import static ru.alex.testcasebankapp.util.Constant.DEFAULT_BALANCE;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    private List<DataValidator> validators;

    private List<UpdateComponent> updateComponents;

    private List<AddComponent> addComponents;

    private List<DeleteComponent> deleteComponents;


    public DefaultUserService(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              List<DataValidator> validators,
                              List<UpdateComponent> updateComponents,
                              List<AddComponent> addComponents,
                              List<DeleteComponent> deleteComponents) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validators = validators;
        this.updateComponents = updateComponents;
        this.addComponents = addComponents;
        this.deleteComponents = deleteComponents;
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
    @Transactional
    public Map<String, String> save(ru.alex.testcasebankapp.model.dto.UserDto userDto, BindingResult bindingResult) {
        validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.error("::SavedException:: \"user not saved because: {}\"", bindingResult.getFieldError().getDefaultMessage());
            throw new SavedException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        User user = UserMapper.INSTANCE.fromDto(userDto);

        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        user.setPhones(GenerateData.generatePhoneEntities(user.getPhones(), user));
        user.setEmails(GenerateData.generateEmailEntities(user.getEmails(), user));
        user.setAccounts(GenerateData.generateAccountEntity(user, DEFAULT_BALANCE));

        userRepository.save(user);

        return Map.of(
                "login", user.getLogin(),
                "role", user.getRole()
        );
    }

    @Override
    public List<UserDto> searchClient(SearchEntity searchEntity, PaginationEntity paginationEntity) {
        return UserMapper.INSTANCE.toDtos(getUsers(searchEntity, paginationEntity));
    }

    private List<User> getUsers(SearchEntity searchEntity, PaginationEntity paginationEntity) {
        switch (searchEntity.chooseType()) {
            case EMAIL -> {
                return List.of(userRepository.findByEmailsIn(searchEntity.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found")));
            }
            case PHONE -> {
                return List.of(userRepository.findByPhone(searchEntity.getPhone())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found")));
            }
            case FULLNAME -> {
                return userRepository.findAllById(userRepository.findAllByFullNameIsLikeId(searchEntity.getFullName(),
                                GenerateData.generatePageRequest(paginationEntity)));
            }
            case DATE -> {
                return userRepository.findAllById(userRepository.findAllByDataOfBirthGreaterThanId(searchEntity.getDate(),
                                GenerateData.generatePageRequest(paginationEntity)));
            }
            default -> {
                return userRepository.findAllById(userRepository.findAllId(GenerateData.generatePageRequest(paginationEntity)));
            }
        }
    }

    @Override
    @Transactional
    public boolean update(ru.alex.testcasebankapp.model.dto.UserDto userDto,
                          Authentication authentication,
                          BindingResult bindingResult) {

        validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UpdateException(bindingResult.getFieldError().getDefaultMessage());
        }

        User user = findByLogin(authentication.getName());

        for (var i : updateComponents) {
            i.execute(userDto, user);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean add(ru.alex.testcasebankapp.model.dto.UserDto userDto,
                       Authentication authentication,
                       BindingResult bindingResult) {
        validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UpdateException(bindingResult.getFieldError().getDefaultMessage());
        }

        User user = findByLogin(authentication.getName());

        for (var i : addComponents) {
            i.execute(userDto, user);
        }
        return true;
    }

    @Override
    public Account findAccountByCard(Authentication authentication, String card) {
        User userDto = findByLogin(authentication.getName());

        return userDto.getAccounts()
                .stream()
                .filter(e -> e.getCard().equals(card))
                .reduce((e, a) -> {
                    throw new UsernameNotFoundException("user not found");
                }).orElseThrow(() -> new TransactionNotFoundException("transaction not found"));
    }

    @Override
    @Transactional
    public boolean delete(ru.alex.testcasebankapp.model.dto.UserDto userDto, Authentication authentication, BindingResult bindingResult) {

        User user = findByLogin(authentication.getName());

        for (var i : deleteComponents) {
            i.execute(userDto, user);
        }
        return true;
    }



    private void validate(ru.alex.testcasebankapp.model.dto.UserDto userDto, BindingResult bindingResult) {
        for (var i : validators) {
            i.validate(userDto, bindingResult);
        }
    }


}
