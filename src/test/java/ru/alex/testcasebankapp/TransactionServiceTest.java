package ru.alex.testcasebankapp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.service.impl.DefaultTransactionService;
import ru.alex.testcasebankapp.util.exception.TransactionException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:15")
                    .withInitScript("data.sql");

    private String  DEFAULT_NUMBER_CARD_FROM_USER = "6923005754745405";
    private String  DEFAULT_NUMBER_CARD_TO_USER = "0971093327478357";
    private UUID DEFAULT_ID_FROM_USER = UUID.fromString("a0601c0e-6c65-4678-8ad5-877636298127");
    private UUID DEFAULT_ID_TO_USER = UUID.fromString("398cb056-fa4c-4c9f-a6f4-ea0e226e5b4c");

    @Mock
    private UserService userService;

    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DefaultTransactionService moneyTransferService;

    @BeforeEach
    void setUp() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(POSTGRESQL_CONTAINER.getJdbcUrl());// Получить реальный jdbcUrl
        hikariConfig.setUsername(POSTGRESQL_CONTAINER.getUsername());// Получить username
        hikariConfig.setPassword(POSTGRESQL_CONTAINER.getPassword());// Получить password
        var dataSource = new HikariDataSource(hikariConfig);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        moneyTransferService.setJdbcTemplate(this.jdbcTemplate);
        addUserInDb(150, 150);
    }

    @Transactional
    public void addUserInDb (double balanceUserOne,
                             double balanceUserTwo) {
        try {
            this.jdbcTemplate.update(
                    "INSERT INTO bank_api.t_account(id, card, initial_deposit, current_balance) VALUES (?, ?, ?, ?)",
                    DEFAULT_ID_FROM_USER, DEFAULT_NUMBER_CARD_FROM_USER, balanceUserOne, balanceUserOne);

            this.jdbcTemplate.update(
                    "INSERT INTO bank_api.t_account(id, card, initial_deposit, current_balance) VALUES (?, ?, ?, ?)",
                    DEFAULT_ID_TO_USER, DEFAULT_NUMBER_CARD_TO_USER, balanceUserTwo, balanceUserTwo);
        } catch (DataAccessException e) {
            // Обработка ошибок, если необходимо
            throw new RuntimeException("Error inserting users into the database", e);
        }
    }

    @Test
    public void transactionTransfer_shouldCompletedTransferMoney_whenBalanceFromUserEqualAmount() {
        addNewBalanceUserInDb(150, 150);

        UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, DEFAULT_NUMBER_CARD_TO_USER, 150.0);
        assertEquals(resultFunctionTransactionTransfer.getClass(), UUID.class);

    }

    @Test
    public void transactionTransfer_shouldCompletedTransferMoney_whenBalanceAmountLessFromUser() {
        addNewBalanceUserInDb(150, 150);

        UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, DEFAULT_NUMBER_CARD_TO_USER, 100.0);
        assertEquals(resultFunctionTransactionTransfer.getClass(), UUID.class);

    }

    @Test
    public void transactionTransfer_shouldNonCompletedTransferMoney_whenBalanceFromUserLessAmount() {
        addNewBalanceUserInDb(150, 150);

        TransactionException transactionException = assertThrows(TransactionException.class, () -> {
            UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, DEFAULT_NUMBER_CARD_TO_USER, 200.0);
        });
        assertEquals("insufficient funds", transactionException.getMessage());

    }

    @Test
    public void transactionTransfer_shouldNonCompletedTransferMoney_whenOneOfTheUserNotFound() {
        addNewBalanceUserInDb(150, 150);

        TransactionException transactionException = assertThrows(TransactionException.class, () -> {
            UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, "", 150.0);
        });
        assertEquals("Receiver card not found", transactionException.getMessage());

    }

    @Test
    public void transactionTransfer_shouldNonCompletedTransferMoney_whenAmountIsNegative() {
        addNewBalanceUserInDb(150, 150);

        TransactionException transactionException = assertThrows(TransactionException.class, () -> {
            UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, DEFAULT_NUMBER_CARD_TO_USER, -50.0);
        });
        assertEquals("Transfer amount must be positive or not equals 0", transactionException.getMessage());
    }

    @Test
    public void transactionTransfer_shouldNonCompletedTransferMoney_whenTransferToSameAccount() {
        addNewBalanceUserInDb(150, 150);

        TransactionException transactionException = assertThrows(TransactionException.class, () -> {
            UUID resultFunctionTransactionTransfer = moneyTransferService.transactionTransfer(DEFAULT_NUMBER_CARD_FROM_USER, DEFAULT_NUMBER_CARD_FROM_USER, 50.0);
        });
        assertEquals("Cannot transfer to the same account", transactionException.getMessage());
    }

    @Transactional
    public void addNewBalanceUserInDb (double balanceUserOne,
                             double balanceUserTwo) {
        try {
            this.jdbcTemplate.update(
                    "UPDATE bank_api.t_account SET current_balance=? WHERE id=?",
                    balanceUserOne,
                    DEFAULT_ID_FROM_USER);

            this.jdbcTemplate.update(
                    "UPDATE bank_api.t_account SET current_balance=? WHERE id=?",
                    balanceUserTwo,DEFAULT_ID_TO_USER);
        } catch (DataAccessException e) {
            // Обработка ошибок, если необходимо
            throw new RuntimeException("Error inserting users into the database", e);
        }
    }

    @Transactional
    public void deleteUserFromDb(UUID idOne, UUID idTwo) {
        try {
            this.jdbcTemplate.update("DELETE FROM bank_api.t_account WHERE id=?", idOne);
            this.jdbcTemplate.update("DELETE FROM bank_api.t_account WHERE id=?", idTwo);
        } catch (DataAccessException e) {
            // Обработка ошибок, если необходимо
            throw new RuntimeException("Error deleting users from the database", e);
        }
    }


}
