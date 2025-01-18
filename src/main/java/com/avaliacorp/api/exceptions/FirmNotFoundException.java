package com.avaliacorp.api.exceptions;

public class FirmNotFoundException extends RuntimeException {
    public FirmNotFoundException(){
        super("Firm was not found");
    }
    public FirmNotFoundException(String message){
        super(message);
    }
}
