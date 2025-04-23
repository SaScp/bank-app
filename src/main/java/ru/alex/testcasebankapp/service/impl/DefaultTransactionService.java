package ru.alex.testcasebankapp.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.entity.AmountEntity;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.service.TransactionService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.service.rowmapper.JsonNodeRowMapper;
import ru.alex.testcasebankapp.util.Constant.*;
import ru.alex.testcasebankapp.util.exception.InsufficientFundsException;
import ru.alex.testcasebankapp.util.exception.TransactionException;
import ru.alex.testcasebankapp.util.exception.TransactionNotFoundException;


import java.sql.*;
import java.util.List;
import java.util.UUID;

import static ru.alex.testcasebankapp.util.Constant.*;

@Service
@Slf4j
public class DefaultTransactionService implements TransactionService {


    private UserService userService;

    private JdbcTemplate jdbcTemplate;

    public DefaultTransactionService(UserService userService,
                                     JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UUID transferMoney(Authentication fromAuthentication, AmountEntity amountEntity) {
        String toCard = amountEntity.getToCard();
        String fromCard = amountEntity.getFromCard();
        double amount = amountEntity.getAmount();
        Account account = userService.findAccountByCard(fromAuthentication, fromCard);
        return transactionTransfer(account.getCard(), toCard, amount);
    }

    public UUID transactionTransfer(String fromCard, String toCard, double amount) {

        return jdbcTemplate.execute((Connection connection) -> {
            try (PreparedStatement ps1 = connection.prepareStatement(SELECT_CURRENT_BALANCE_BY_CARD);
                 PreparedStatement ps2 = connection.prepareStatement(UPDATE_BALANCE_BY_CARD);
                 PreparedStatement transaction = connection.prepareStatement(CREATE_INFO_ABOUT_TRANSACTION)) {
                connection.setAutoCommit(false);
                if (amount <= 0) {
                    throw new RuntimeException("Transfer amount must be positive or not equals 0");
                }
                if (fromCard.equals(toCard)) {
                    throw new RuntimeException("Cannot transfer to the same account");
                }

                ps1.setString(1, fromCard);
                ResultSet rs = ps1.executeQuery();
                if (!rs.next()) {
                    connection.rollback();
                    throw new RuntimeException("Sender card not found");
                }
                double fromUserBalance = rs.getDouble(1);

                if (fromUserBalance < amount) {
                    connection.rollback();
                    throw new InsufficientFundsException("insufficient funds");
                }

                ps2.setDouble(1, fromUserBalance - amount);
                ps2.setString(2, fromCard);
                ps2.executeUpdate();

                ps1.setString(1, toCard);
                rs = ps1.executeQuery();
                if (!rs.next()) {
                    connection.rollback();
                    throw new TransactionException("Receiver card not found");
                }
                double toUserBalance = rs.getDouble(1);


                ps2.setDouble(1, amount + toUserBalance);
                ps2.setString(2, toCard);
                ps2.executeUpdate();

                UUID transactionId = UUID.randomUUID();
                transaction.setObject(1, transactionId);
                transaction.setString(2, fromCard);
                transaction.setString(3, toCard);
                transaction.setDouble(4, amount);
                transaction.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                transaction.executeUpdate();

                connection.commit();
                log.info("transaction with id: {} registration!", transactionId);
                return transactionId;
            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                log.error("::TransactionException:: \"transaction failed, error message: {}\"", e.getMessage());
                throw new TransactionException(e.getMessage());
            }
        });
    }

    @Override
    public JsonNode getTransactionById(String id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_TRANSACTION_BY_ID, new JsonNodeRowMapper(), UUID.fromString(id));
        } catch (Exception e) {
            throw new TransactionNotFoundException("not found");
        }
    }


    public List<JsonNode> getAllTransactions() {
        return jdbcTemplate.query(SELECT_ALL_TRANSACTION, new JsonNodeRowMapper());
    }

    @Override
    public List<JsonNode> getUserTransactionByCard(Authentication authentication, String currentCard) {
        String card = userService.findAccountByCard(authentication, currentCard).getCard();
        return jdbcTemplate.query(SELECT_TRANSACTION_BY_FROM_USER_CARD_OR_TO_USER_CARD, new JsonNodeRowMapper(), card, card);
    }


    @Scheduled(fixedRate = 60000)
    @Async
    public void updateBalance() {
        jdbcTemplate
                .update(UPDATE_ALL_BALANCE_ON_FIVE_PROCENT);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
