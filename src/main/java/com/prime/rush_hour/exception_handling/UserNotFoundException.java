package com.prime.rush_hour.exception_handling;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
    }
}
