package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<UserGetDto> get();
    UserGetDto get(String email);
    UserGetDto create(UserPostDto userPostDto);
    void delete(Integer Id);
    UserGetDto update(Integer id, UserPutDto userPutDtoDtoDto);
}
