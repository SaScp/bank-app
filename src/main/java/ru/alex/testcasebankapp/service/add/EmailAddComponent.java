package ru.alex.testcasebankapp.service.add;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.EmailRepository;

import java.util.Optional;

@Component
public class EmailAddComponent implements AddComponent {

    private EmailRepository emailRepository;

    public EmailAddComponent(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        if (Optional.ofNullable(updateUserDtoDto.getEmails()).isPresent()) {
            emailRepository.saveAll(updateUserDtoDto.getEmails().stream()
                    .filter(emailDto -> Optional.ofNullable(emailDto.getEmail()).isPresent())
                    .map(el -> Email.builder().email(el.getEmail()).user(userDto).build()).toList());
        }
    }
}
