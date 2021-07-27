package com.prime.rush_hour.services;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements  UserService{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: Vidi dal je neophodno ovako il ce svj da vrati praznu listu
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById() {
        return null;
    }

    @Override
    public User createOrUpdateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());

        if(existingUser.isPresent()){
            User updatedUser = getUpdatedUser(existingUser, user);
            return userRepository.save(updatedUser);
        }
        return userRepository.save(user);
    }

    private User getUpdatedUser(Optional<User> existingUser, User user) {
        User updatedUser = existingUser.get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRoles(user.getRoles());
        updatedUser.setAppointments(user.getAppointments());
        return updatedUser;
    }

    @Override
    public void deleteUserById() {

    }
}
