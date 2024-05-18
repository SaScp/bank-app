package ru.alex.testcasebankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
    Optional<Phone> findByPhone(String phone);

    Optional<Phone> findByPhoneAndUser(String phone, User user);

    List<Phone> findAllByPhoneInAndUser(List<String> phones, User user);
}
