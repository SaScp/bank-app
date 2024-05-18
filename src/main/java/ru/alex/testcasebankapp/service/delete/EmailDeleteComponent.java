package ru.alex.testcasebankapp.service.delete;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.EmailDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.EmailRepository;

import java.util.*;

@Component
public class EmailDeleteComponent implements DeleteComponent{


    private EmailRepository emailRepository;

    public EmailDeleteComponent(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void execute(UserDto updateUserDto, User user) {
        List<UUID> idDeleteEmails = new ArrayList<>();
        if (Optional.ofNullable(updateUserDto.getEmails()).isPresent()) {
            List<String> emails = updateUserDto.getEmails().stream()
                    .map(EmailDto::getEmail)
                    .filter(Objects::nonNull)
                    .toList();

            if (!emails.isEmpty()) {
                List<Email> emailEntities = emailRepository.findAllByEmailInAndUser(emails, user);
                if (emailEntities.size() == 1) {
                    return;
                }
                idDeleteEmails = emailEntities.stream()
                        .map(Email::getId)
                        .toList();
            }
        }
        if (!idDeleteEmails.isEmpty()) {
            emailRepository.deleteAllByIdInBatch(idDeleteEmails);
        }
    }
}
