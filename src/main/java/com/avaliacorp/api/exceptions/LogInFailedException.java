package com.avaliacorp.api.exceptions;

public class LogInFailedException extends RuntimeException {
    public LogInFailedException(){
        super("It was not possible to LogIn, wrong password");
    }
}
