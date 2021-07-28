package com.prime.rush_hour.controllers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.dtos.UserGetDto;
import com.prime.rush_hour.mapstruct.mappers.MapStructMapper;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.services.UserService;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private MapStructMapper mapStructMapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMapStructMapper(MapStructMapper mapStructMapper) {
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(mapStructMapper.userToUserGetDto(userService.getUserById(id)), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        return new ResponseEntity<User>(userService.createUser(user), new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody @Valid User user){
        return new ResponseEntity<User>(userService.updateUser(id, user), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") Integer id){
            userService.deleteUserById(id);
            return new ResponseEntity(String.format("User with the id %d successfully deleted", id), HttpStatus.OK);
    }
}
