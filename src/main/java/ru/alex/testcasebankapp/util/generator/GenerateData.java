package ru.alex.testcasebankapp.util.generator;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateData {
    public static PageRequest generatePageRequest(PaginationEntity paginationEntity) {
        if (paginationEntity.getPropertySort().isEmpty()) {
            return PageRequest.of(
                    paginationEntity.getPageNumber(),
                    paginationEntity.getPageSize()
            );
        } else {
            return PageRequest.of(
                    paginationEntity.getPageNumber(),
                    paginationEntity.getPageSize(),
                    Sort.by(paginationEntity.getPropertySort()).ascending()
            );
        }
    }

    public static Set<Email> generateEmailEntities(Set<Email> emails, User user) {
        return emails.stream().map(email -> {
            email.setUser(user);
            return email;
        }).collect(Collectors.toSet());
    }

    public static Set<Phone> generatePhoneEntities(Set<Phone> phones, User user) {
        return phones.stream().map(email -> {
            email.setUser(user);
            return email;
        }).collect(Collectors.toSet());
    }

    public static Account generateAccountEntity(User user, int currentBalance) {
        return Account.builder()
                .card(generateNumberCard())
                .user(user)
                .currentBalance(currentBalance)
                .initialDeposit(currentBalance)
                .build();
    }

    public static String generateNumberCard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(new Random().nextInt());
        }
        return builder.toString();
    }
}
