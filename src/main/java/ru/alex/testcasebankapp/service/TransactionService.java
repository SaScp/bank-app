package ru.alex.testcasebankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.Authentication;
import ru.alex.testcasebankapp.model.entity.AmountEntity;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    UUID transferMoney(Authentication fromAuthentication, AmountEntity amountEntity);

    void updateBalance();

    List<JsonNode> getAllTransactions();

    List<JsonNode> getUserTransaction(Authentication authentication);

    UUID transactionTransfer(String fromCard, String toCard, double amount);

    JsonNode getTransactionById(String id);
}
