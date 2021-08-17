package com.prime.rush_hour.mapstruct.mappers;

import com.prime.rush_hour.dtos.ActivityGetDto;
import com.prime.rush_hour.dtos.ActivityPostDto;
import com.prime.rush_hour.dtos.ActivityPutDto;
import com.prime.rush_hour.entities.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//TODO: Mozda ce biti potrebni jos neki argumenti ovde, vidi to kad dodje vrijeme.
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityMapper {
    ActivityGetDto activityToActivityGetDto(Activity activity);
    List<ActivityGetDto> activitiesToActivityGetDtos(List<Activity> activities);
    Activity activityPostDtoToActivity(ActivityPostDto activityPostDto);
    void updateActivity(ActivityPutDto activityPutDto, @MappingTarget Activity activity);
}
