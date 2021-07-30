package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
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
    public ResponseEntity<UserGetDto> get(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDto> update(@PathVariable Integer id, @RequestBody @Valid UserPutDto userPutDto){
        return ResponseEntity.ok(userService.update(id, userPutDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
            userService.delete(id);
            return ResponseEntity.ok().build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
