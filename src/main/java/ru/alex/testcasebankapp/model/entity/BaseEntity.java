package ru.alex.testcasebankapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(updatable = false, name = "updated_at")
    private LocalDateTime updateAt;
}
