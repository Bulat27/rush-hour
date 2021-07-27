package com.prime.rush_hour.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Appointment {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "appointment_activities",
               joinColumns = @JoinColumn(name = "appointment_id"),
               inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private Set<Activity> activities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }
}
