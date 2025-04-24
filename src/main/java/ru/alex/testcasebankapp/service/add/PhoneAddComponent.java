package ru.alex.testcasebankapp.service.add;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.PhoneRepository;

import java.util.Optional;

@Component
public class PhoneAddComponent implements AddComponent {

    private PhoneRepository phoneRepository;

    public PhoneAddComponent(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        if (Optional.ofNullable(updateUserDtoDto.getPhones()).isPresent()) {
        phoneRepository.saveAll(updateUserDtoDto.getPhones().stream()
                .filter(phoneDto -> Optional.ofNullable(phoneDto.getPhone()).isPresent())
                .map(el -> Phone.builder().phone(el.getPhone()).user(userDto).build()).toList());
    }
        }
}
