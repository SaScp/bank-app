package ru.alex.testcasebankapp;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.alex.testcasebankapp.service.UserService;
import ru.alex.testcasebankapp.service.impl.DefaultTransactionService;

import javax.sql.DataSource;
import java.sql.SQLException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TransferTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserService userService;

    @InjectMocks
    private DefaultTransactionService moneyTransferService;

    @BeforeEach
    public void init() {
        this.jdbcTemplate.update("INSERT INTO bank_api.t_account(id, card, initial_deposit, current_balance) VALUES ('a0601c0e-6c65-4678-8ad5-877636298127', '6923005754745405', 150.0, 150.0);\n" +
                "INSERT INTO bank_api.t_account(id, card, initial_deposit, current_balance) VALUES ('398cb056-fa4c-4c9f-a6f4-ea0e226e5b4c', '0971093327478357', 150.0, 150.0);");
    }

    @Test
    void testCompleteTransaction() throws SQLException {
        boolean transactionTransfer = moneyTransferService.transactionTransfer("6923005754745405", "0971093327478357", 100);
        System.out.println(transactionTransfer);
        Assertions.assertFalse(transactionTransfer);
    }
}
