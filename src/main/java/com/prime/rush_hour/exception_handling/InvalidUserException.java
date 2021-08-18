package com.prime.rush_hour.exception_handling;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(){
        super("You can only use the crud commands on your account");
    }
}
