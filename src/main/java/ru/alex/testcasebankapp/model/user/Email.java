package ru.alex.testcasebankapp.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alex.testcasebankapp.model.dto.EmailDto;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_email", schema = "bank_api")
public class Email implements Comparable<EmailDto>{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @jakarta.validation.constraints.Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public int compareTo(EmailDto o) {
        return this.email.compareTo(o.getOldEmail() == null? o.getEmail() : o.getOldEmail());
    }
}
