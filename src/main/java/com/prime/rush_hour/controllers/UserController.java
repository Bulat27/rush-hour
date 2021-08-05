package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.security.authentication.MyUserDetails;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

//TODO: Razni statusni kodovi (npr na signup-u) nisu kako treba. Radi na tome kad dodje vreme.
@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserGetDto>> get(){
        return ResponseEntity.ok(userService.get());
    }

    @PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserGetDto> get(@PathVariable String email, Authentication authentication) {
          return ResponseEntity.ok(userService.get(email));
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
