package com.avaliacorp.api.exceptions;

/**
 * Exceção disparada na criação de um usuário / empresa, ou na alteração dos dados de um usuário / empresa, para indicar quando um email já cadastrado no banco de dados.
 */
public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(){
        super("The email is already in use");
    }

    public EmailAlreadyInUseException(String message){ // Construtor com mensagem personalizada
        super(message);
    }

}
