package com.avaliacorp.api.exceptions;

/**
 * Exceção que representa uma ação proibida dentro da logica da api, tipo uma empresa tentar postar uma valiação.
 */
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message){
        super(message);
    }
}
