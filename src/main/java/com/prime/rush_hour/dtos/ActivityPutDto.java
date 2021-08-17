package com.prime.rush_hour.dtos;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

public class ActivityPutDto {

    //TODO: Vidi da li su potrebne jos neke validacije
    //@NotBlank
    private String name;
    private Duration duration;
    private Double price;

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
