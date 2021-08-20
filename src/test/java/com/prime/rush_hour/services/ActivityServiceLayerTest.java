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

//TODO: Work on naming. Predefined, fetched...
@ExtendWith(MockitoExtension.class)
class ActivityServiceLayerTest {

    @Mock
    private ActivityRepository activityRepository;

    @Spy
    private ActivityMapperImpl activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

//    @BeforeAll
//    public void init(){
//        List<Activity> activityList = new ArrayList<>();
//
//        Activity a1 = new Activity(1, "Trcanje", Duration.of(50, ChronoUnit.MINUTES), 25.4);
//        Activity a2 = new Activity(2, "Skakanje", Duration.of(40, ChronoUnit.MINUTES), 25.7);
//        Activity a3 = new Activity(3, "Preticanje", Duration.of(30, ChronoUnit.MINUTES), 25.8);
//
//        activityList.add(a1);
//        activityList.add(a2);
//        activityList.add(a3);
//
//        when(activityRepository.findAll()).thenReturn(activityList);
//    }

    @Test
    void canGetAllActivities(){
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
//        assertThat(activityList.size()).isEqualTo(actList.size());
//        verify(activityRepository, times(1)).findAll();
//        verify(activityMapper, times(1)).activitiesToActivityGetDtos(activityList);
    }

    @Test
    void canGetActivityByName(){
        Activity a1 = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);

        when(activityRepository.findByName(a1.getName())).thenReturn(Optional.of(a1));

        ActivityGetDto activityGetDto = activityService.get(a1.getName());

        assertThat(activityGetDto).usingRecursiveComparison().isEqualTo(activityMapper.activityToActivityGetDto(a1));
//        verify(activityRepository, times(1)).findByName(a1.getName());
//        verify(activityMapper, times(1)).activityToActivityGetDto(a1);
    }

    //TODO: Promeni i ovde u fiksni String kao i u Service-u
    @Test
    void willThrowWhenNameDoesntExist(){
        when(activityRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.get(anyString()))
                .isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void canAddActivity(){
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
    void canUpdateActivity(){
        Activity existingActivity = new Activity(1, "Haircut", Duration.of(50, MINUTES), 25.4);
        when(activityRepository.findByName(existingActivity.getName())).thenReturn(Optional.of(existingActivity));
        when(activityRepository.save(existingActivity)).thenReturn(existingActivity);

        ActivityPutDto newActivity = new ActivityPutDto("Shaving", Duration.of(40, MINUTES), 25.7);

        ActivityGetDto updatedActivity = activityService.update(existingActivity.getName(), newActivity);

        assertThat(updatedActivity).usingRecursiveComparison().isEqualTo(existingActivity);
    }

    @Test
    void canDeleteByName(){
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
