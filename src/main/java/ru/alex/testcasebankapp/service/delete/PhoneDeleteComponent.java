package ru.alex.testcasebankapp.service.delete;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.PhoneDto;
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
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        List<UUID> idDeletePhones = new ArrayList<>();
        if (Optional.ofNullable(updateUserDtoDto.getPhones()).isPresent()) {
            if (userDto.getPhones().size() == 1) {
                throw new LastElementException("user must be 1 phone!");
            }
            List<String> phones = updateUserDtoDto.getPhones().stream()
                    .map(PhoneDto::getPhone)
                    .filter(Objects::nonNull)
                    .toList();

            if (!phones.isEmpty()) {
                List<Phone> phoneEntities = phoneRepository.findAllByPhoneInAndUser(phones, userDto);
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
