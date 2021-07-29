package com.prime.rush_hour.services;

import com.prime.rush_hour.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> getAllUsers();
    User getUserById(Integer id);
    User createUser(User user);
    void deleteUserById(Integer Id);
    User updateUser (Integer id, User newUser);
}
