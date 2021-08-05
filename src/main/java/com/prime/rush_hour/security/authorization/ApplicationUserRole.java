package com.prime.rush_hour.security.authorization;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;



public enum ApplicationUserRole {
    USER,
    ADMIN

//    private final Set<ApplicationUserPermission> permissions;
//
//    ApplicationUserRole(Set<ApplicationUserPermission> permissions){
//        this.permissions = permissions;
//    }
//
//    public Set<ApplicationUserPermission> getPermissions() {
//        return permissions;
//    }
//
//    public Collection<SimpleGrantedAuthority> getGrantedAuthorities(){
//        //TODO: Vidi dal je bolje da bude neka druga struktura
//        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
//        for (ApplicationUserPermission permission : getPermissions()) {
//               authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
//        }
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return authorities;
//    }
}
