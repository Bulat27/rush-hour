package com.prime.rush_hour.controllers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.dtos.UserGetDto;
import com.prime.rush_hour.mapstruct.dtos.UserPostDto;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMapStructMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserGetDto>> getAllUsers(){
        return ResponseEntity.ok(userMapper.usersToUserGetDtos(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userMapper.userToUserGetDto(userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        return new ResponseEntity<User>(userService.createUser(user), new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Integer id, @RequestBody @Valid UserPostDto userPostDto){
        userService.updateUser(id, userMapper.userPostDtoToUser(userPostDto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") Integer id){
            userService.deleteUserById(id);
            return new ResponseEntity(String.format("User with the id %d successfully deleted", id), HttpStatus.OK);
    }
}
