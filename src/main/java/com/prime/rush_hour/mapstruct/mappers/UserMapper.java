package com.prime.rush_hour.mapstruct.mappers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserGetDto userToUserGetDto(User user);
    User userPostDtoToUser(UserPostDto userPostDto);
    List<UserGetDto> usersToUserGetDtos(List<User> users);
}
