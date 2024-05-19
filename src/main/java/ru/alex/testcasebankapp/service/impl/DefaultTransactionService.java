package ru.alex.testcasebankapp.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.entity.AmountEntity;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.service.TransactionService;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.service.rowmapper.JsonNodeRowMapper;
import ru.alex.testcasebankapp.util.exception.TransactionException;


import java.sql.*;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultTransactionService implements TransactionService {

    private UserService userService;

    private JdbcTemplate jdbcTemplate;

    public DefaultTransactionService(UserService userService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean transferMoney(Authentication fromAuthentication, AmountEntity amountEntity) {
        String toCard = amountEntity.getToCard();
        double amount = amountEntity.getAmount();
        Account account = userService.findByLogin(fromAuthentication.getName()).getAccount();
        return Boolean.TRUE.equals(jdbcTemplate.execute((Connection connection) -> {
            try (PreparedStatement ps1 = connection.prepareStatement("SELECT current_balance FROM bank_api.t_account WHERE card = ?");
                 PreparedStatement ps2 = connection.prepareStatement("UPDATE bank_api.t_account SET current_balance = ? WHERE card = ?");
                 PreparedStatement transaction = connection.prepareStatement("INSERT INTO bank_api.t_transaction (id, from_user_card, to_user_card, amount, created_at) VALUES (?, ?, ?, ?, ?)")) {
                connection.setAutoCommit(false);

                // Get balance of sender
                ps1.setString(1, account.getCard());
                ResultSet rs = ps1.executeQuery();
                if (!rs.next()) {
                    connection.rollback();
                    throw new RuntimeException("Sender card not found");
                }
                double fromUserBalance = rs.getDouble(1);

                if (fromUserBalance < amount) {
                    connection.rollback();
                    return false;
                }

                // Update balance of sender
                ps2.setDouble(1, fromUserBalance - amount);
                ps2.setString(2, account.getCard());
                ps2.executeUpdate();

                // Get balance of receiver
                ps1.setString(1, toCard);
                rs = ps1.executeQuery();
                if (!rs.next()) {
                    connection.rollback();
                    throw new TransactionException("Receiver card not found");
                }
                double toUserBalance = rs.getDouble(1);

                // Update balance of receiver
                ps2.setDouble(1, amount + toUserBalance);
                ps2.setString(2, toCard);
                ps2.executeUpdate();

                // Insert transaction record
                transaction.setObject(1, UUID.randomUUID());
                transaction.setString(2, account.getCard());
                transaction.setString(3, toCard);
                transaction.setDouble(4, amount);
                transaction.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                transaction.executeUpdate();

                connection.commit();
                return true;
            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                throw new TransactionException("transaction error");
            }
        }));
    }

    public List<JsonNode> getAllTransactions() {
        return jdbcTemplate.query("SELECT * FROM bank_api.t_transaction", new JsonNodeRowMapper());
    }

    @Override
    public List<JsonNode> getUserTransaction(Authentication authentication) {
        String card = userService.findByLogin(authentication.getName()).getAccount().getCard();
        return jdbcTemplate.query("SELECT * FROM bank_api.t_transaction WHERE from_user_card=? or to_user_card=?", new JsonNodeRowMapper(), card, card);
    }


    @Scheduled(fixedRate = 60000)
    @Async
    public void updateBalance() {
        jdbcTemplate
                .update("UPDATE bank_api.t_account SET current_balance = current_balance + (current_balance * 0.05) WHERE current_balance < initial_deposit * 2.07");
    }
}
