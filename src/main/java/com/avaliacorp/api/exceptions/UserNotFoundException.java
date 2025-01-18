package com.avaliacorp.api.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("The user was not found");
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
