package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import com.prime.rush_hour.entities.Activity;
import com.prime.rush_hour.exception_handling.ActivityExistsException;
import com.prime.rush_hour.exception_handling.ActivityNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.ActivityMapperImpl;
import com.prime.rush_hour.repositories.ActivityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private Activity activity;
    private static ActivityPostDto activityPostDto;

    @BeforeEach
    public void initBeforeEach(){
        activity = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);
    }

    @BeforeAll
    public static void initBeforeAll(){
        activityPostDto = new ActivityPostDto(1, "Haircut", Duration.of(50, MINUTES), 25.4);
    }

    @Test
    void getAllShouldReturnAllActivities(){
        List<Activity> predefinedList = new ArrayList<>();

        Activity a2 = new Activity(2, "Shaving", Duration.of(40, MINUTES), 25.7);
        Activity a3 = new Activity(3, "Beard trimming", Duration.of(30, MINUTES), 25.8);

        predefinedList.add(activity);
        predefinedList.add(a2);
        predefinedList.add(a3);

        when(activityRepository.findAll()).thenReturn(predefinedList);

        List<ActivityGetDto> fetchedList = activityService.get();

        assertThat(activityMapper.activitiesToActivityGetDtos(predefinedList)).usingRecursiveComparison().isEqualTo(fetchedList);
    }

    @Test
    void getByNameShouldReturnActivityByName(){
        when(activityRepository.findByName(activity.getName())).thenReturn(Optional.of(activity));

        ActivityGetDto activityGetDto = activityService.get(activity.getName());

        assertThat(activityGetDto).usingRecursiveComparison().isEqualTo(activityMapper.activityToActivityGetDto(activity));
    }

    @Test
    void willThrowWhenNameDoesntExist(){
        when(activityRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.get("Haircut"))
                .isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void createActivityShouldAddActivity(){
        activityService.create(activityPostDto);

        ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(activityArgumentCaptor.capture());
        Activity capturedActivity = activityArgumentCaptor.getValue();

        assertThat(activityMapper.activityPostDtoToActivity(activityPostDto)).usingRecursiveComparison().isEqualTo(capturedActivity);
    }

    @Test
    void willThrowWhenNameAlreadyExists(){
        when(activityRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> activityService.create(activityPostDto)).isInstanceOf(ActivityExistsException.class);
    }

    @Test
    void updateByNameShouldUpdateAllTheFieldsOfTheActivity(){
        when(activityRepository.findByName(activity.getName())).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);

        ActivityPutDto newActivity = new ActivityPutDto("Shaving", Duration.of(40, MINUTES), 25.7);

        ActivityGetDto updatedActivity = activityService.update(activity.getName(), newActivity);

        assertThat(updatedActivity).usingRecursiveComparison().isEqualTo(activity);
    }

    @Test
    void updateByNameShouldUpdateSomeFieldsOfTheActivity(){
        when(activityRepository.findByName(activity.getName())).thenReturn(Optional.of(activity));
        when(activityRepository.save(activity)).thenReturn(activity);

        ActivityPutDto newActivity = new ActivityPutDto(null, Duration.of(40, MINUTES), 25.7);

        ActivityGetDto updatedActivity = activityService.update(activity.getName(), newActivity);

        assertThat(updatedActivity).usingRecursiveComparison().isEqualTo(activity);
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
