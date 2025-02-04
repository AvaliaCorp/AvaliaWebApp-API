package com.avaliacorp.api.exceptions;

/**
 * Exceção disparada no registro de uma empresa (firm) ou na alteração de dados quando o CNPJ passado já está cadastrado no banco de dados.
 * 
 * É usada unicamente na classe FirmService.
 */
public class CnpjAlreadyInUseException extends RuntimeException {
    public CnpjAlreadyInUseException(){
        super("CNPJ is already in use.");
    }
}
