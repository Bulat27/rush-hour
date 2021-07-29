package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public List<UserGetDto> get() {
        return userMapper.usersToUserGetDtos(userRepository.findAll());
    }

    @Override
    public UserGetDto get(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) return userMapper.userToUserGetDto(user.get());
        throw new UserNotFoundException(id);
    }

    @Override
    public UserGetDto create(UserPostDto userPostDto) {
        User user = userMapper.userPostDtoToUser(userPostDto);
        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public void update(Integer id, UserPostDto userPostDto){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            userPostDto.setId(id);
            userRepository.save(userMapper.userPostDtoToUser(userPostDto));
            return;
        }

        throw new UserNotFoundException(id);
    }

//    private User getUpdated(Optional<User> existingUser, User newUser) {
//        User updatedUser = existingUser.get();
//        updatedUser.setFirstName(newUser.getFirstName());
//        updatedUser.setLastName(newUser.getLastName());
//        updatedUser.setEmail(newUser.getEmail());
//        updatedUser.setPassword(newUser.getPassword());
//        return updatedUser;
//    }

    @Override
    public void delete(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent())
            userRepository.deleteById(id);
        else
            throw new UserNotFoundException(id);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
