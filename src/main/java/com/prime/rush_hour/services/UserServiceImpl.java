package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserGetDto> get() {
        return userMapper.usersToUserGetDtos(userRepository.findAll());
    }

    @Override
    public UserGetDto get(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public UserGetDto create(UserPostDto userPostDto) {
        User user = userMapper.userPostDtoToUser(userPostDto);
        //TODO: Implement the logic to prevent the same username(email)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    //TODO: Encode the password here too
    @Override
    public UserGetDto update(Integer id, UserPutDto userPutDto){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userMapper.update(userPutDto, user);
        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public void delete(Integer id) {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
