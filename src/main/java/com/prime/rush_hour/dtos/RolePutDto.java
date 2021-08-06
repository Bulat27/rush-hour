package com.prime.rush_hour.dtos;

import com.prime.rush_hour.security.authorization.ApplicationUserRole;

public class RolePutDto {

    private ApplicationUserRole name;

    public ApplicationUserRole getName() {
        return name;
    }

    public void setName(ApplicationUserRole name) {
        this.name = name;
    }
}
