package ru.alex.testcasebankapp.service;

import org.springframework.security.core.Authentication;

public interface TransactionService {
    boolean transferMoney(Authentication fromAuthentication, String toCard, double amount);
}
