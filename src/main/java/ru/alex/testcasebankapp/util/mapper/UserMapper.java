package ru.alex.testcasebankapp.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.alex.testcasebankapp.model.dto.UserAdminDto;
import ru.alex.testcasebankapp.model.dto.UserDto;
import ru.alex.testcasebankapp.model.user.User;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "accounts", source = "accounts")
    UserDto toDto(User userDto);

    User fromDto(UserDto userDto);

    List<UserDto> toDtos(List<User> users);

    List<User> fromDtos(List<UserDto> users);

    User fromAdminDto(UserAdminDto adminDto);
}
