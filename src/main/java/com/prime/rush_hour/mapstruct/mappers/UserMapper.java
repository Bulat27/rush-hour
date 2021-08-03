package com.prime.rush_hour.mapstruct.mappers;

import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.security.authentication.MyUserDetails;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserGetDto userToUserGetDto(User user);
    User userPostDtoToUser(UserPostDto userPostDto);
    List<UserGetDto> usersToUserGetDtos(List<User> users);
    void update(UserPutDto userPutDto, @MappingTarget User user);
    @Mappings({
            @Mapping(target = "username", source = "email")
    })
    MyUserDetails userToMyUserDetails(User user);
}
