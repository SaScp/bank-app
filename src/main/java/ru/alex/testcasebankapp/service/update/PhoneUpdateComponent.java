package ru.alex.testcasebankapp.service.update;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.user.Phone;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.repository.PhoneRepository;

import java.util.*;

@Component
public class PhoneUpdateComponent implements UpdateComponent {

    private PhoneRepository phoneRepository;

    public PhoneUpdateComponent(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void execute(ru.alex.testcasebankapp.model.dto.UserDto updateUserDtoDto, User userDto) {
        List<Phone> entitiesToUpdate = new ArrayList<>();
        if (Optional.ofNullable(updateUserDtoDto.getPhones()).isPresent()) {
            for (var i : updateUserDtoDto.getPhones()) {
                if (Optional.ofNullable(i.getPhone()).isPresent()) {
                    phoneRepository.findByPhoneAndUser(i.getOldPhone(), userDto)
                            .ifPresent(
                                    el -> {
                                        el.setPhone(i.getPhone());
                                        entitiesToUpdate.add(el);
                                    }
                            );
                }
            }
            if (!entitiesToUpdate.isEmpty()) {
                phoneRepository.saveAll(entitiesToUpdate);
            }
        }
    }
}
