package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
