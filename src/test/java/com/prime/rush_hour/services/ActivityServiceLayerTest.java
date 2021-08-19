package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.entities.Activity;
import com.prime.rush_hour.exception_handling.ActivityNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.ActivityMapperImpl;
import com.prime.rush_hour.repositories.ActivityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceLayerTest {

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
    public void canGetAllActivities(){
        List<Activity> activityList = new ArrayList<>();

        Activity a1 = new Activity(1, "Trcanje", Duration.of(50, ChronoUnit.MINUTES), 25.4);
        Activity a2 = new Activity(2, "Skakanje", Duration.of(40, ChronoUnit.MINUTES), 25.7);
        Activity a3 = new Activity(3, "Preticanje", Duration.of(30, ChronoUnit.MINUTES), 25.8);

        activityList.add(a1);
        activityList.add(a2);
        activityList.add(a3);

        when(activityRepository.findAll()).thenReturn(activityList);

        List<ActivityGetDto> actList = activityService.get();

        assertThat(activityList.size()).isEqualTo(actList.size());
        verify(activityRepository, times(1)).findAll();
        verify(activityMapper, times(1)).activitiesToActivityGetDtos(activityList);
    }

    @Test
    public void canGetActivityByName(){
        Activity a1 = new Activity(1, "Trcanje", Duration.of(50, ChronoUnit.MINUTES), 25.4);

        when(activityRepository.findByName(a1.getName())).thenReturn(Optional.of(a1));

        ActivityGetDto activityGetDto = activityService.get(a1.getName());

        assertThat(activityGetDto.getName()).isEqualTo(a1.getName());
        verify(activityRepository, times(1)).findByName(a1.getName());
        verify(activityMapper, times(1)).activityToActivityGetDto(a1);
    }

    @Test
    void willThrowWhenNameDoesntExist(){
        when(activityRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.get(anyString()))
                .isInstanceOf(ActivityNotFoundException.class);
    }

}
