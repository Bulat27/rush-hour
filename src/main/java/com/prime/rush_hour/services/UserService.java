package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.RolePutDto;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<UserGetDto> get();
    UserGetDto get(String email);
    UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes);
    void delete(String email);
    UserGetDto update(UserPutDto userPutDto);
    void updateRoles(String email, List<RolePutDto> rolePutDtos);
}
