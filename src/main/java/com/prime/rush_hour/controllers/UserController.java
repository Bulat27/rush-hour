package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.RolePutDto;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.exception_handling.InvalidUserException;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    //@PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<UserGetDto> get(@PathVariable String email, Authentication auth) {
        if(!isAdmin(auth) && !isLoggedInUser(auth, email)) throw new InvalidUserException();
        return ResponseEntity.ok(userService.get(email));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto, List.of(ApplicationUserRole.ADMIN,ApplicationUserRole.USER)));
    }

//    @PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    //@PutMapping("/{email}")
    @PutMapping
    public ResponseEntity<UserGetDto> update(@RequestBody @Valid UserPutDto userPutDto, Authentication auth){
        boolean isAdmin = isAdmin(auth);
        if(!isAdmin && !isLoggedInUser(auth, userPutDto.getEmail())) throw new InvalidUserException();

        //TODO: Vidi kako se handle-a exception
        if(!isAdmin && userPutDto.getRoles() != null) throw new IllegalArgumentException("User cannot update the roles");

        return ResponseEntity.ok(userService.update(userPutDto));
    }

    //@PreAuthorize("#email == authentication.principal or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email, Authentication auth){
        if(!isAdmin(auth) && !isLoggedInUser(auth, email)) throw new InvalidUserException();

        userService.delete(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserGetDto> signUp(@RequestBody @Valid UserPostDto userPostDto){
        return ResponseEntity.ok(userService.create(userPostDto, List.of(ApplicationUserRole.USER)));
    }

    private boolean isAdmin(Authentication auth){
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + ApplicationUserRole.ADMIN.name()));
    }

    private boolean isLoggedInUser(Authentication auth, String email){
        return email.equals(auth.getPrincipal());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
