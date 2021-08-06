package com.prime.rush_hour.exception_handling;

public class AdminCannotBeDeletedException extends RuntimeException{

    public AdminCannotBeDeletedException(){
        super("Initial admin cannot be deleted");
    }
}
