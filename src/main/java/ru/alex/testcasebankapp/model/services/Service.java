package ru.alex.testcasebankapp.model.services;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "t_services", schema = "bank_api")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "c_percent", nullable = false)
    private Double percent;


    @Column(name = "c_type", nullable = false)
    private Long type;
}
