package ru.alex.testcasebankapp.service.delete;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.EmailDto;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.EmailRepository;
import ru.alex.testcasebankapp.util.exception.LastElementException;

import java.util.*;

@Component
public class EmailDeleteComponent implements DeleteComponent{


    private EmailRepository emailRepository;

    public EmailDeleteComponent(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        List<UUID> idDeleteEmails = new ArrayList<>();
        if (Optional.ofNullable(updateUserDtoDto.getEmails()).isPresent()) {
            if (userDto.getEmails().size() == 1) {
                throw new LastElementException("user must be 1 email!");
            }
            List<String> emails = updateUserDtoDto.getEmails().stream()
                    .map(EmailDto::getEmail)
                    .filter(Objects::nonNull)
                    .toList();

            if (!emails.isEmpty()) {
                List<Email> emailEntities = emailRepository.findAllByEmailInAndUser(emails, userDto);
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
