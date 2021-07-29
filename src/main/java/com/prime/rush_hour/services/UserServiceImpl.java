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
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public List<User> get() {
        List<User> allUsers = userRepository.findAll();
        if(allUsers.isEmpty()) throw new NoDataFoundException();
        return allUsers;
    }

    @Override
    public User get(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) return user.get();
        throw new UserNotFoundException(id);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Integer id, User newUser){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            User updatedUser = getUpdated(existingUser, newUser);
            return userRepository.save(updatedUser);
        }
        throw new UserNotFoundException(id);
    }

    private User getUpdated(Optional<User> existingUser, User newUser) {
        User updatedUser = existingUser.get();
        updatedUser.setFirstName(newUser.getFirstName());
        updatedUser.setLastName(newUser.getLastName());
        updatedUser.setEmail(newUser.getEmail());
        updatedUser.setPassword(newUser.getPassword());
        return updatedUser;
    }

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
}
