package ru.alex.testcasebankapp.service.add;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.UserDto;
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
    public void execute(UserDto updateUserDto, User user) {
        if (Optional.ofNullable(updateUserDto.getEmails()).isPresent()) {
            emailRepository.saveAll(updateUserDto.getEmails().stream()
                    .filter(emailDto -> Optional.ofNullable(emailDto.getEmail()).isPresent())
                    .map(el -> Email.builder().email(el.getEmail()).user(user).build()).toList());
        }
    }
}
