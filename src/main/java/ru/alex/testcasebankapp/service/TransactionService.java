package ru.alex.testcasebankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.Authentication;
import ru.alex.testcasebankapp.model.entity.AmountEntity;

import java.util.List;

public interface TransactionService {
    boolean transferMoney(Authentication fromAuthentication, AmountEntity amountEntity);
    void updateBalance();
    List<JsonNode> getAllTransactions();
    List<JsonNode> getUserTransaction(Authentication authentication);
}
