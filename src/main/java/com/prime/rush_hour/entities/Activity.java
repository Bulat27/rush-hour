package com.prime.rush_hour.entities;

import org.springframework.boot.convert.DurationUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Entity(name = "activities")
public class Activity {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @DurationUnit(ChronoUnit.MINUTES)
    @Column(nullable = false)
    private Duration duration;

    @Column(nullable = false)
    private Double price;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
