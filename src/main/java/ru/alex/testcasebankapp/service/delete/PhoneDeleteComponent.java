package ru.alex.testcasebankapp.service.delete;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.EmailDto;
import ru.alex.testcasebankapp.model.dto.PhoneDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.Email;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.PhoneRepository;

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
            List<String> phones = updateUserDto.getPhones().stream()
                    .map(PhoneDto::getPhone)
                    .filter(Objects::nonNull)
                    .toList();

            if (!phones.isEmpty()) {
                List<Phone> phoneEntities = phoneRepository.findAllByPhoneInAndUser(phones, user);
                if (phoneEntities.size() == 1) {
                    return;
                }
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
