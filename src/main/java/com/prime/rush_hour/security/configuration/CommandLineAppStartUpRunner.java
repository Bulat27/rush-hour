package com.prime.rush_hour.security.configuration;

import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.prime.rush_hour.security.authorization.ApplicationUserRole.*;

@Component
public class CommandLineAppStartUpRunner implements CommandLineRunner {

    private UserService userService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private InitialAdminConfig initialAdminConfig;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.existsByEmail(initialAdminConfig.getEmail())) return;

        roleRepository.save(new Role(ADMIN));
        roleRepository.save(new Role(USER));

        UserPostDto adminUser = new UserPostDto(initialAdminConfig.getName(), initialAdminConfig.getLastname(), initialAdminConfig.getEmail(), initialAdminConfig.getPassword());
        userService.create(adminUser, Arrays.asList(ADMIN, USER));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setInitialAdminConfig(InitialAdminConfig initialAdminConfig) {
        this.initialAdminConfig = initialAdminConfig;
    }
}
