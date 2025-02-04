package com.avaliacorp.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.CnpjAlreadyInUseException;
import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.PostRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class FirmService {

    private final FirmRepository firmRepository;
    private final PostRepository postRepository;

    public FirmService(FirmRepository firmRepository, PostRepository postRepository){
        this.firmRepository = firmRepository;
        this.postRepository = postRepository;
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
    public Metrics calcMetric(String cnpj){
        List<PostModel> posts = postRepository.findByFkCNPJ(cnpj);
        if(posts.isEmpty()){
            throw new NotFoundException("There isn't any Post with the CNPJ");
        }
        Double med = 0d;
        Integer aboveMed = 0;
        Integer belowMed = 0;
        for (PostModel post : posts) {
            med = med + post.getGrade();
            if(post.getGrade() > 5){
                aboveMed++;
            }
            if(post.getGrade() < 6){
                belowMed++;
            }
        }
        med = med/posts.size();
        Double highestGrade = postRepository.getMaxGradeWithCNPJFilter(cnpj);
        Double lowestGrade = postRepository.getMinGradeWithCNPJFilter(cnpj);
        Metrics metrics = new Metrics(med, aboveMed, belowMed, posts.size(), highestGrade, lowestGrade);
        return metrics;
    }
    public record Metrics(Double med, Integer numAboveMedium, Integer numBelowMedium, Integer total, Double highestGrade, Double lowestGrade){}

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
