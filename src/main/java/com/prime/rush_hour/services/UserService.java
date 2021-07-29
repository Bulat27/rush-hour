package com.prime.rush_hour.services;

import com.prime.rush_hour.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> get();
    User get(Integer id);
    User create(User user);
    void delete(Integer Id);
    User update(Integer id, User newUser);
}
