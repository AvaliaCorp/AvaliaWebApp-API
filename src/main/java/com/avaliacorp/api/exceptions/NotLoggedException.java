package com.avaliacorp.api.exceptions;

public class NotLoggedException extends RuntimeException {
    public NotLoggedException(){
        super("The user is not logged.");
    }
}
