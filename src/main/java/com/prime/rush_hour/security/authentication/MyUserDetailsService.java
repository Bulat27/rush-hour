package com.prime.rush_hour.security.authentication;

import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Not found " + userName));
        MyUserDetails userDetails = userMapper.userToMyUserDetails(user);
        userDetails.setAuthorities(getAuthorities(user));
        return userDetails;
    }

     private Collection<GrantedAuthority> getAuthorities(User user){
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<Role> roles = user.getRoles();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
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
