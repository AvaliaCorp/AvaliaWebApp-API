package com.avaliacorp.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.CnpjAlreadyInUseException;
import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.repositories.FirmRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class FirmService {

    private final FirmRepository firmRepository;

    public FirmService(FirmRepository firmRepository){
        this.firmRepository = firmRepository;
    }

    @Transactional
    public FirmModel create(FirmModel firm) throws CnpjAlreadyInUseException, EmailAlreadyInUseException {
        firmRepository.findByEmail(firm.getEmail()).ifPresent(err -> {
            throw new EmailAlreadyInUseException("The email is already in use.");
        });

        firmRepository.findByCNPJ(firm.getCNPJ()).ifPresent(err -> {
            throw new CnpjAlreadyInUseException();
        });

        String uuid = UUID.randomUUID().toString();
        String hashedPassword = BCrypt.withDefaults().hashToString(12, firm.getPassword().toCharArray());
        firm.setId(uuid);
        firm.setName(firm.getName().toLowerCase());
        firm.setPassword(hashedPassword);
        return firmRepository.save(firm);
    }

    @Transactional
    public FirmModel findById(String id) throws NotFoundException {
        return firmRepository.findById(id).orElseThrow(() -> new NotFoundException("The Firm Id was not found"));
    }

    @Transactional
    public FirmModel findByCNPJ(String CNPJ) throws NotFoundException {
        return firmRepository.findByCNPJ(CNPJ).orElseThrow(() -> new NotFoundException("The CNPJ was not found"));
    }

    @Transactional
    public List<FirmModel> searchByName(String name, Integer limit) throws IllegalArgumentException {
        if(name == null){
            throw new IllegalArgumentException("Name param must not null");
        }
        return firmRepository.findManyByName(name + "%", limit);
    }

    @Transactional
    public void delete(String id) throws NotFoundException {
        firmRepository.findById(id).orElseThrow(() -> new NotFoundException("The firm id was not found"));
        firmRepository.deleteById(id);
    }

}
