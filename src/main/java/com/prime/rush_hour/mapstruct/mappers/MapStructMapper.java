package com.prime.rush_hour.mapstruct.mappers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.dtos.UserGetDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MapStructMapper {
    UserGetDto userToUserGetDto(User user);
}
