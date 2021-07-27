package com.prime.rush_hour.services;

import com.prime.rush_hour.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    //TODO: Vidi za RecordNotFoundException
    List<User> getAllUsers();
    User getUserById(Integer id) throws  RuntimeException;
    User createUser(User user);
    void deleteUserById(Integer Id) throws RuntimeException;
    User updateUser (Integer id, User newUser) throws RuntimeException;
}
