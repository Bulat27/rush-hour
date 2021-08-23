package com.prime.rush_hour.repositories;

import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ApplicationUserRole name);
}
