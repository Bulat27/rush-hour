package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import com.prime.rush_hour.entities.Activity;
import com.prime.rush_hour.exception_handling.ActivityExistsException;
import com.prime.rush_hour.exception_handling.ActivityNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.ActivityMapper;
import com.prime.rush_hour.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService{

    private ActivityRepository activityRepository;
    private ActivityMapper activityMapper;

    @Override
    public List<ActivityGetDto> get() {
        return activityMapper.activitiesToActivityGetDtos(activityRepository.findAll());
    }

    @Override
    public ActivityGetDto get(String name) {
        Activity activity = activityRepository.findByName(name).orElseThrow(() -> new ActivityNotFoundException(name));

        return activityMapper.activityToActivityGetDto(activity);
    }

    @Override
    public ActivityGetDto create(ActivityPostDto activityPostDto) {
        if(activityRepository.existsByName(activityPostDto.getName())) throw new ActivityExistsException(activityPostDto.getName());

        Activity activity = activityMapper.activityPostDtoToActivity(activityPostDto);
        return activityMapper.activityToActivityGetDto(activityRepository.save(activity));
    }

    @Override
    public ActivityGetDto update(String name, ActivityPutDto activityPutDto) {
        Activity activity = activityRepository.findByName(name).orElseThrow(() -> new ActivityNotFoundException(name));

        activityMapper.updateActivity(activityPutDto, activity);
        return activityMapper.activityToActivityGetDto(activityRepository.save(activity));
    }

    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Autowired
    public void setActivityMapper(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }
}
