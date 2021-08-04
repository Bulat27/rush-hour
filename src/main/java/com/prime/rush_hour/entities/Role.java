package com.prime.rush_hour.entities;

import com.prime.rush_hour.security.authorization.ApplicationUserRole;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
//    private String name;
    private ApplicationUserRole name;

    //TODO: Mozda i ovde
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }


    public ApplicationUserRole getName() {
        return name;
    }

    public void setName(ApplicationUserRole name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
