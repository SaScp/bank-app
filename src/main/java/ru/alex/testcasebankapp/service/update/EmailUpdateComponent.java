package ru.alex.testcasebankapp.service.update;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.EmailDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.EmailRepository;

import java.util.*;

@Component
public class EmailUpdateComponent implements UpdateComponent {

    private EmailRepository emailRepository;

    public EmailUpdateComponent(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void execute(UserDto updateUserDto, User user) {
        List<Email> entitiesToUpdate = new ArrayList<>();
        if (Optional.ofNullable(updateUserDto.getEmails()).isPresent()) {
            for (var i : updateUserDto.getEmails()) {
                if (Optional.ofNullable(i.getEmail()).isPresent()
                        && Optional.ofNullable(i.getOldEmail()).isPresent()) {
                    emailRepository.findByEmailAndUser(i.getOldEmail(), user)
                            .ifPresent(el -> {
                                el.setEmail(i.getEmail());
                               entitiesToUpdate.add(el);
                            });
                }
            }
            if (!entitiesToUpdate.isEmpty()) {
                emailRepository.saveAll(entitiesToUpdate);
            }
        }
    }
}
