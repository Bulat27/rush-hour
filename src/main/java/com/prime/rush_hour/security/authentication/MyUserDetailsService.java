package com.prime.rush_hour.security.authentication;

import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    //private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserMapper userMapper;

//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        return new MyUserDetails(s, passwordEncoder);
        //TODO: Handle this in ContollerAdvisor
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Not found " + userName));
        return userMapper.userToMyUserDetails(user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
