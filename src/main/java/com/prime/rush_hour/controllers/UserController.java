package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.RolePutDto;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

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
        return ResponseEntity.ok(userService.create(userPostDto, Arrays.asList(ApplicationUserRole.ADMIN,ApplicationUserRole.USER)));
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{email}/roles")
    public ResponseEntity<Void> updateRoles(@PathVariable String email, @RequestBody List<RolePutDto> rolePutDtos){
        userService.updateRoles(email, rolePutDtos);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
