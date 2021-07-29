package com.prime.rush_hour.services;

import com.prime.rush_hour.exception_handling.NoDataFoundException;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if(allUsers.isEmpty()) throw new NoDataFoundException();
        return allUsers;
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) return user.get();
        throw new UserNotFoundException(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User newUser){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            User updatedUser = getUpdatedUser(existingUser, newUser);
            return userRepository.save(updatedUser);
        }
        throw new UserNotFoundException(id);
    }

    private User getUpdatedUser(Optional<User> existingUser, User newUser) {
        User updatedUser = existingUser.get();
        updatedUser.setFirstName(newUser.getFirstName());
        updatedUser.setLastName(newUser.getLastName());
        updatedUser.setEmail(newUser.getEmail());
        updatedUser.setPassword(newUser.getPassword());
        return updatedUser;
    }

    @Override
    public void deleteUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent())
            userRepository.deleteById(id);
        else
            throw new UserNotFoundException(id);
    }
}
