package com.prime.rush_hour.exception_handling;

public class AdminCannotBeModifiedException extends RuntimeException{

    public AdminCannotBeModifiedException(){
        super("Initial admin cannot be modified");
    }
}
