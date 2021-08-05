package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.security.authentication.MyUserDetails;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserGetDto>> get(){
        return ResponseEntity.ok(userService.get());
    }

    @PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserGetDto> get(@PathVariable String email, Authentication authentication) {
          return ResponseEntity.ok(userService.get(email));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto, ApplicationUserRole.ADMIN));
    }

    @PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @PutMapping("/{email}")
    public ResponseEntity<UserGetDto> update(@PathVariable String email, @RequestBody @Valid UserPutDto userPutDto){
        return ResponseEntity.ok(userService.update(email, userPutDto));
    }

    @PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email){
            userService.delete(email);
            return ResponseEntity.ok().build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
