package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.CnpjAlreadyInUseException;
import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.services.FirmService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/firm")
public class FirmController {

    private final FirmService firmService;

    public FirmController(FirmService firmService){
        this.firmService = firmService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerFirm(@RequestBody FirmModel firm) {

        try {
            FirmModel result = firmService.create(firm);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } 
        catch (Exception e) {
            if(e instanceof EmailAlreadyInUseException){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            if(e instanceof CnpjAlreadyInUseException){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/get")
    public ResponseEntity<Object> getByCNPJ(@RequestParam String CNPJ) {

        try {
            FirmModel firm = firmService.findByCNPJ(CNPJ);
            return ResponseEntity.status(HttpStatus.OK).body(firm);
        }
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchFirm(@RequestParam String name, @RequestParam Integer limit) {

        try {
            List<FirmModel> firms = firmService.searchByName(name, limit);
            return ResponseEntity.ok().body(firms);
        }
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFirm(@RequestParam String id){

        try {
            firmService.delete(id);
            return ResponseEntity.ok().body("Account deleted successfully.");    
        } 
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
