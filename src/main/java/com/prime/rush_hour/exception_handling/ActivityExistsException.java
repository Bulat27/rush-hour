package com.prime.rush_hour.exception_handling;

public class ActivityExistsException extends RuntimeException{
    public ActivityExistsException(String name){
        super("Activity " + name + " already exists");
    }
}
