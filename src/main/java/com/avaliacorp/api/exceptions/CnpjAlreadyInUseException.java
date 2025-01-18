package com.avaliacorp.api.exceptions;

public class CnpjAlreadyInUseException extends RuntimeException {
    public CnpjAlreadyInUseException(){
        super("CNPJ is already in use.");
    }
    public CnpjAlreadyInUseException(String message){
        super(message);
    }
}
