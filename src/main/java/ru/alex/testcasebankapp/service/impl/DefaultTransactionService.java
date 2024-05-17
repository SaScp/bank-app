package ru.alex.testcasebankapp.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.alex.testcasebankapp.model.user.Account;
import ru.alex.testcasebankapp.service.TransactionService;
import ru.alex.testcasebankapp.service.UserService;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
    public boolean transferMoney(Authentication fromAuthentication, String toCard, double amount) {
        Account account = userService.findByLogin(fromAuthentication.getName()).getAccount();
        return Boolean.TRUE.equals(jdbcTemplate.execute((Connection connection) -> {
            try (PreparedStatement ps1 = connection.prepareStatement("SELECT current_balance FROM bank_api.t_account WHERE card = ?");
                 PreparedStatement ps2 = connection.prepareStatement("UPDATE bank_api.t_account SET current_balance = ? WHERE card = ?");
                 PreparedStatement transaction = connection.prepareStatement("INSERT INTO transactions (id, from_user_id, to_user_id, amount, created_at) VALUES (?, ?, ?, ?, ?)")) {
                connection.setAutoCommit(false);

                ps1.setString(1, account.getCard());
                double fromUserBalance = ps1.executeQuery().getDouble(1);

                if (fromUserBalance < amount) {
                    connection.rollback();
                    return false;
                }

                ps2.setDouble(1, fromUserBalance - amount);
                ps2.setString(2, account.getCard());
                ps2.executeUpdate();

                ps1.setString(1, toCard);
                double toUserBalance = ps1.executeQuery().getDouble(1);
                ps2.setDouble(1, amount + toUserBalance);
                ps2.setString(2, toCard);
                ps2.executeUpdate();

                transaction.setObject(1, UUID.randomUUID());
                transaction.setString(2, account.getCard());
                transaction.setString(3, toCard);
                transaction.setDouble(4, amount);
                transaction.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                transaction.executeUpdate();

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException();
            }
        }));

    }

    @Scheduled(fixedDelayString = "4s")
    @Async
    public void update() {

    }
}
