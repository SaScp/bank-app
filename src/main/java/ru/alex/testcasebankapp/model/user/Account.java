package ru.alex.testcasebankapp.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "t_account", schema = "bank_api")
public class Account {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "card")
    private String card;

    @Column(name = "initial_deposit")
    private double initialDeposit;

    @Column(name = "current_balance")
    private double currentBalance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Account() {

    }
}
