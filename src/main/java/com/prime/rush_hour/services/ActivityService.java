package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {

    List<ActivityGetDto> get();
    ActivityGetDto get(String name);
    ActivityGetDto create(ActivityPostDto activityPostDto);
    ActivityGetDto update(String name, ActivityPutDto activityPutDto);
    void delete(String name);
}
