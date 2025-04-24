package ru.alex.testcasebankapp.service.update;

import org.springframework.stereotype.Component;
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
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        List<Email> entitiesToUpdate = new ArrayList<>();
        if (Optional.ofNullable(updateUserDtoDto.getEmails()).isPresent()) {
            for (var i : updateUserDtoDto.getEmails()) {
                if (Optional.ofNullable(i.getEmail()).isPresent()
                        && Optional.ofNullable(i.getOldEmail()).isPresent()) {
                    emailRepository.findByEmailAndUser(i.getOldEmail(), userDto)
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
