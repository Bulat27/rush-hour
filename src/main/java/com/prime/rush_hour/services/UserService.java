package com.prime.rush_hour.services;

import com.prime.rush_hour.exception_handling.NoDataFoundException;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    //TODO: Vidi dal da brises ove throws...
    List<User> getAllUsers() throws NoDataFoundException;
    User getUserById(Integer id) throws UserNotFoundException;
    User createUser(User user);
    void deleteUserById(Integer Id) throws UserNotFoundException;
    User updateUser (Integer id, User newUser) throws UserNotFoundException;
}
