package com.prime.rush_hour.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import serialization.ActivityPutDtoDeserializer;

import java.time.Duration;

@JsonDeserialize(using = ActivityPutDtoDeserializer.class)
public class ActivityPutDto {

    private String name;
    private Duration duration;
    private Double price;

    public ActivityPutDto() {}

    public ActivityPutDto(String name, Duration duration, Double price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
