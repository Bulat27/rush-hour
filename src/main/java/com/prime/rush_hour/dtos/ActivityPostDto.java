package com.prime.rush_hour.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import serialization.ActivityPostDtoDeserializer;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@JsonDeserialize(using = ActivityPostDtoDeserializer.class)
public class ActivityPostDto {

    @Id
    @GeneratedValue
    private Integer id;

    //TODO : Vidi jos za ove validacije. Tipa max cena, max trajanje itd.
    @NotBlank
    private String name;

    @NotNull
    private Duration duration;

    @NotNull
    private Double price;

    public ActivityPostDto(String name, Duration duration, Double price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public ActivityPostDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
