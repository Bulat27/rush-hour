package com.prime.rush_hour.controllers;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import com.prime.rush_hour.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private ActivityService activityService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ActivityGetDto>> get(){
        return ResponseEntity.ok(activityService.get());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<ActivityGetDto> get(@PathVariable String name){
        return ResponseEntity.ok(activityService.get(name));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    //TODO: Vidi dal je potrebna jos neka validacija
    public ResponseEntity<ActivityGetDto> create (@RequestBody @Valid ActivityPostDto activityPostDto){
        return ResponseEntity.ok(activityService.create(activityPostDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{name}")
    public ResponseEntity<ActivityGetDto> update(@PathVariable String name, @RequestBody @Valid ActivityPutDto activityPutDto){
        return ResponseEntity.ok(activityService.update(name, activityPutDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name){
        return null;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
}
