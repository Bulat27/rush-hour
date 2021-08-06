package com.prime.rush_hour.security.configuration;

import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import com.prime.rush_hour.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.prime.rush_hour.security.authorization.ApplicationUserRole.*;

@Component
public class CommandLineAppStartUpRunner implements CommandLineRunner {

    private UserService userService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        //if(userRepository.existsByEmail("nbulat99@gmail.com")) return;
//        addRoles();
//        addAdmin();
        if(userRepository.existsByEmail("nbulat99@gmail.com")) return;

        roleRepository.save(new Role(ADMIN));
        roleRepository.save(new Role(USER));

        //TODO: Izvuci ovo u config ili makar zakucaj stringove.
        UserPostDto adminUser = new UserPostDto("Nikola", "Bulat", "nbulat99@gmail.com", "nikolabulat");
        userService.create(adminUser, Arrays.asList(ADMIN, USER));
    }
//    //TODO: Izmeni tako da koristis metode iz UserServiceImpl kad ih doteras. Napravi konstruktor sa poljima
//    //TODO: Ovde je dosta toga bilo za testiranje, posle promeni i prilagodi
//    private void addAdmin(){
//        Set<Role> roles = new HashSet<>();
////        roles.add(new Role(ADMIN));
////        roles.add(new Role(USER));
//        Role adminRole = roleRepository.findByName(ADMIN).orElseThrow();
//        Role userRole = roleRepository.findByName(USER).orElseThrow();
//        roles.add(adminRole);
//        roles.add(userRole);
//        String password = passwordEncoder.encode("Banovobrdo27");
//        User adminUser = new User("Nikola", "Bulat", "nbulat99@gmail.com", password, roles);
//        userRepository.save(adminUser);
//    }
//
//    private void addRoles(){
//        roleRepository.save(new Role(ADMIN));
//        roleRepository.save(new Role(USER));
//    }

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
}
