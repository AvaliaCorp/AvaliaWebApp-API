package com.avaliacorp.api.exceptions;

/**
 * Exceção que representa uma ação proibida dentro da logica da api, tipo uma empresa tentar postar uma valiação.
 */
public class ForbbidenActionException extends RuntimeException {
    public ForbbidenActionException(String message){
        super(message);
    }
}
