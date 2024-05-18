package ru.alex.testcasebankapp.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.user.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);

    @Query("select user from User user join user.emails e where :email = e.email")
    Optional<User> findByEmailsIn(@Param("email") String email);

    @Query("select user from User user join user.phones p where :phone = p.phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query("select user from User user where user.fullName like :name")
    Optional<List<User>> findAllByFullNameIsLike(@Param("name") String name);

    Optional<List<User>> findAllByDataOfBirthGreaterThan(LocalDateTime localDateTime);

}
