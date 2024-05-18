package ru.alex.testcasebankapp.service;

import org.springframework.security.core.Authentication;
import ru.alex.testcasebankapp.model.entity.AmountEntity;

public interface TransactionService {
    boolean transferMoney(Authentication fromAuthentication, AmountEntity amountEntity);
}
