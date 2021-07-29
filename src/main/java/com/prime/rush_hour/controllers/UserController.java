package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
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

    @GetMapping
    public ResponseEntity<List<UserGetDto>> get(){
        return ResponseEntity.ok(userService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Integer id, @RequestBody @Valid UserPostDto userPostDto){
        userService.update(id, userPostDto);
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
}
