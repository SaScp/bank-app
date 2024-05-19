package ru.alex.testcasebankapp.service.delete;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.EmailDto;
import ru.alex.testcasebankapp.model.dto.PhoneDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.PhoneRepository;
import ru.alex.testcasebankapp.util.exception.LastElementException;

import java.util.*;

@Component
public class PhoneDeleteComponent implements DeleteComponent {

    private PhoneRepository phoneRepository;

    public PhoneDeleteComponent(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void execute(UserDto updateUserDto, User user) {
        List<UUID> idDeletePhones = new ArrayList<>();
        if (Optional.ofNullable(updateUserDto.getPhones()).isPresent()) {
            if (user.getPhones().size() == 1) {
                throw new LastElementException("user must be 1 phone!");
            }
            List<String> phones = updateUserDto.getPhones().stream()
                    .map(PhoneDto::getPhone)
                    .filter(Objects::nonNull)
                    .toList();

            if (!phones.isEmpty()) {
                List<Phone> phoneEntities = phoneRepository.findAllByPhoneInAndUser(phones, user);
                idDeletePhones = phoneEntities.stream()
                        .map(Phone::getId)
                        .toList();
            }
        }
        if (!idDeletePhones.isEmpty()) {
            phoneRepository.deleteAllByIdInBatch(idDeletePhones);
        }
    }
}
