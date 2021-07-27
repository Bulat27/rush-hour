package com.prime.rush_hour.controllers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(userService.getAllUsers(),new HttpHeaders(), HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody User user){
        return new ResponseEntity<User>(userService.createOrUpdateUser(user), new HttpHeaders(), HttpStatus.OK);
    }

}
