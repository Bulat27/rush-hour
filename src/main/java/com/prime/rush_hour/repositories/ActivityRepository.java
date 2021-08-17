package com.prime.rush_hour.repositories;

import com.prime.rush_hour.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Optional<Activity> findByName(String name);
    boolean existsByName(String name);
}
