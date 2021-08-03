package com.prime.rush_hour.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
//    private final PasswordEncoder passwordEncoder;

//    public MyUserDetails(String userName, PasswordEncoder passwordEncoder) {
//        this.userName = userName;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO: Menjaj ovo kad implementiras uloge
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
//        return passwordEncoder.encode("password");
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
