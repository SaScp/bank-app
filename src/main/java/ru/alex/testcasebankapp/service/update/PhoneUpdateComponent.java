package ru.alex.testcasebankapp.service.update;

import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.model.dto.PhoneDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
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
    public void execute(UserDto updateUserDto, User user) {
        List<Phone> entitiesToUpdate = new ArrayList<>();
        if (Optional.ofNullable(updateUserDto.getPhones()).isPresent()) {
            for (var i : updateUserDto.getPhones()) {
                if (Optional.ofNullable(i.getPhone()).isPresent()) {
                    phoneRepository.findByPhoneAndUser(i.getOldPhone(), user)
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
