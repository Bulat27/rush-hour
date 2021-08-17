package com.prime.rush_hour.exception_handling;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(String name){
        super("Activity " + name + " not found");
    }
}
