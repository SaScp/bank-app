package ru.alex.testcasebankapp.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.user.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);

    @Query("select user from User user where ?1 in user.emails")
    Optional<User> findByEmails(String email);

    @Query("select user from User user where ?1 in user.phones")
    Optional<User> findByPhones(String phone);

    Optional<List<User>> findAllByFullNameIsLike(String name);

    Optional<List<User>> findAllByDataOfBirthGreaterThan(LocalDateTime localDateTime);

}
