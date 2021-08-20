package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import com.prime.rush_hour.entities.Activity;
import com.prime.rush_hour.exception_handling.ActivityExistsException;
import com.prime.rush_hour.exception_handling.ActivityNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.ActivityMapperImpl;
import com.prime.rush_hour.repositories.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceLayerTest {

    @Mock
    private ActivityRepository activityRepository;

    @Spy
    private ActivityMapperImpl activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;


    @Test
    void getAllShouldReturnAllActivities(){
        List<Activity> predefinedList = new ArrayList<>();

        Activity a1 = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);
        Activity a2 = new Activity(2, "Shaving", Duration.of(40, MINUTES), 25.7);
        Activity a3 = new Activity(3, "Beard trimming", Duration.of(30, MINUTES), 25.8);

        predefinedList.add(a1);
        predefinedList.add(a2);
        predefinedList.add(a3);

        when(activityRepository.findAll()).thenReturn(predefinedList);

        List<ActivityGetDto> fetchedList = activityService.get();

        assertThat(activityMapper.activitiesToActivityGetDtos(predefinedList)).usingRecursiveComparison().isEqualTo(fetchedList);
    }

    @Test
    void getByNameShouldReturnActivityByName(){
        Activity a1 = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);

        when(activityRepository.findByName(a1.getName())).thenReturn(Optional.of(a1));

        ActivityGetDto activityGetDto = activityService.get(a1.getName());

        assertThat(activityGetDto).usingRecursiveComparison().isEqualTo(activityMapper.activityToActivityGetDto(a1));
    }

    @Test
    void willThrowWhenNameDoesntExist(){
        when(activityRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.get("Haircut"))
                .isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void createActivityShouldAddActivity(){
        ActivityPostDto a1 = new ActivityPostDto(1, "Haircut", Duration.of(50, MINUTES), 25.4);

        activityService.create(a1);

        ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(activityArgumentCaptor.capture());
        Activity capturedActivity = activityArgumentCaptor.getValue();

        assertThat(activityMapper.activityPostDtoToActivity(a1)).usingRecursiveComparison().isEqualTo(capturedActivity);
    }

    @Test
    void willThrowWhenNameAlreadyExists(){
        ActivityPostDto a1 = new ActivityPostDto(1, "Haircut", Duration.of(50, MINUTES), 25.4);

        when(activityRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> activityService.create(a1)).isInstanceOf(ActivityExistsException.class);
    }

    @Test
    void updateByNameShouldUpdateAllTheFieldsOfTheActivity(){
        Activity existingActivity = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);
        when(activityRepository.findByName(existingActivity.getName())).thenReturn(Optional.of(existingActivity));
        when(activityRepository.save(existingActivity)).thenReturn(existingActivity);

        ActivityPutDto newActivity = new ActivityPutDto("Shaving", Duration.of(40, MINUTES), 25.7);

        ActivityGetDto updatedActivity = activityService.update(existingActivity.getName(), newActivity);

        assertThat(updatedActivity).usingRecursiveComparison().isEqualTo(existingActivity);
    }

    @Test
    void updateByNameShouldUpdateSomeFieldsOfTheActivity(){
        Activity existingActivity = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);
        when(activityRepository.findByName(existingActivity.getName())).thenReturn(Optional.of(existingActivity));
        when(activityRepository.save(existingActivity)).thenReturn(existingActivity);

        ActivityPutDto newActivity = new ActivityPutDto(null, Duration.of(40, MINUTES), 25.7);

        ActivityGetDto updatedActivity = activityService.update(existingActivity.getName(), newActivity);

        assertThat(updatedActivity).usingRecursiveComparison().isEqualTo(existingActivity);
    }

    @Test
    void deleteByNameShouldDeleteActivity(){
        String name = "Haircut";
        when(activityRepository.existsByName(name)).thenReturn(true);

        activityService.delete(name);

        verify(activityRepository, times(1)).deleteByName(name);
    }

    @Test
    void willThrowWhenDeleteActivityNotFound(){
        String name = "Haircut";
        when(activityRepository.existsByName(name)).thenReturn(false);

        assertThatThrownBy(() -> activityService.delete(name)).isInstanceOf(ActivityNotFoundException.class);
    }
}
