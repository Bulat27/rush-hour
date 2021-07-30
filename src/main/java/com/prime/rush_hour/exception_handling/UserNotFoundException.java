package com.prime.rush_hour.exception_handling;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Integer id) {
        super(String.format("User with Id %d not found", id));
    }
}
