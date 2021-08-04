package com.prime.rush_hour.security.configuration;

import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.prime.rush_hour.security.authorization.ApplicationUserRole.*;

@Component
public class CommandLineAppStartUpRunner implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        addRoles();
        addAdmin();
    }
    //TODO: Izmeni tako da koristis metode iz UserServiceImpl kad ih doteras. Napravi konstruktor sa poljima
    //TODO: Ovde je dosta toga bilo za testiranje, posle promeni i prilagodi
    private void addAdmin(){
        Set<Role> roles = new HashSet<>();
//        roles.add(new Role(ADMIN));
//        roles.add(new Role(USER));
        Role adminRole = roleRepository.findByName(ADMIN).orElseThrow();
        Role userRole = roleRepository.findByName(USER).orElseThrow();
        roles.add(adminRole);
        roles.add(userRole);
        String password = passwordEncoder.encode("Banovobrdo27");
        User adminUser = new User("Nikola", "Bulat", "nbulat99@gmail.com", password, roles);
        userRepository.save(adminUser);
    }

    private void addRoles(){
        Role adminRole = new Role(ADMIN);
        Role userRole = new Role(USER);
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
