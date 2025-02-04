package com.avaliacorp.api.exceptions;

/**
 * Exceção para inidcar que um login falhou por senha incorreta.
 * 
 * Usada unicamento na classe LoginService.
 */
public class LogInFailedException extends RuntimeException {
    public LogInFailedException(){
        super("It was not possible to LogIn, wrong password");
    }
}
