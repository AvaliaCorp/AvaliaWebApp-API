package com.avaliacorp.api.exceptions;

/**
 * Classe para indicar que indicar que um objeto não foi encontrado.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(){
        super("Object not found");
    }
    public NotFoundException(String message){ // Construtor com mensagem personalizada
        super(message);
    }
}
