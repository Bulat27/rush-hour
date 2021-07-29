package com.prime.rush_hour.controllers;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserGetDto>> get(){
        return ResponseEntity.ok(userMapper.usersToUserGetDtos(userService.get()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userMapper.userToUserGetDto(userService.get(id)));
    }

    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        User user = userMapper.userPostDtoToUser(userPostDto);
        userService.create(user);
        return ResponseEntity.ok(userMapper.userToUserGetDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @RequestBody @Valid UserPostDto userPostDto){
        userService.update(id, userMapper.userPostDtoToUser(userPostDto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
            userService.delete(id);
            return ResponseEntity.ok().build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMapStructMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
