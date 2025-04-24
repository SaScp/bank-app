package ru.alex.testcasebankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.User;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    Optional<Email> findByEmail(String email);

    Optional<Email> findByEmailAndUser(String email, User userDto);

    List<Email> findAllByEmailInAndUser(List<String> emails, User userDto);

}
