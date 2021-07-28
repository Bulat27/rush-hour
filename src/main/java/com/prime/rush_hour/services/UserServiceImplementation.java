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
    public User getUserById(Integer id) throws RuntimeException{
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) return user.get();
        throw new RuntimeException("The user record with the specified id doesn't exist");
    }

    @Override
    public User createUser(User user) {
//        Optional<User> existingUser = userRepository.findById(user.getId());
//
//        if(existingUser.isPresent()){
//            User updatedUser = getUpdatedUser(existingUser, user);
//            return userRepository.save(updatedUser);
//        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User newUser) throws RuntimeException {
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            User updatedUser = getUpdatedUser(existingUser, newUser);
            return userRepository.save(updatedUser);
        }
        //TODO: Izvuci ovo kao konstantu(ako bude trebalo)
        throw new RuntimeException("The user record with the specified id doesn't exist");
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
    public void deleteUserById(Integer id) throws RuntimeException {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent())
            userRepository.deleteById(id);
        else
            throw new RuntimeException("The user record with the specified id doesn't exist");
    }
}
