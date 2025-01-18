package com.avaliacorp.api.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(){
        super("The email is already in use");
    }

    public EmailAlreadyInUseException(String message){
        super(message);
    }

}
