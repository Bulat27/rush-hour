package com.prime.rush_hour.exception_handling;

public class NoDataFoundException extends RuntimeException{

    public NoDataFoundException(){
        super("No data found");
    }
}
