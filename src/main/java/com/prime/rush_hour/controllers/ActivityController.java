package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/v1/activities")
public class ActivityController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ActivityGetDto>> get(){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<ActivityGetDto> get(@PathVariable String name){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    //TODO: Vidi dal je potrebna jos neka validacija
    public ResponseEntity<ActivityGetDto> create (@RequestBody @Valid ActivityPostDto activityPostDto){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<ActivityGetDto> update(@RequestBody @Valid ActivityPutDto activityPutDto){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name){
        return null;
    }
}
