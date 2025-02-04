package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.CnpjAlreadyInUseException;
import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.services.FirmService;
import com.avaliacorp.api.services.FirmService.Metrics;
import com.avaliacorp.api.utils.JwtTools;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/firm")
public class FirmController {

    private final FirmService firmService;
    private final JwtTools jwtTools;

    public FirmController(FirmService firmService, JwtTools jwtTools){
        this.firmService = firmService;
        this.jwtTools = jwtTools;
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

    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics(@RequestParam String cnpj){

        try {
            Metrics metrics = firmService.calcMetric(cnpj);
            String message = String.format("Total grades: %d\nAverage grade: %.2f\nNumber of grades above average: %d\nNumber of grades below average: %d\nHighest Grade: %.2f\nLowest Grade: %.2f", metrics.total(), metrics.med(), metrics.numAboveMedium(), metrics.numBelowMedium(), metrics.highestGrade(), metrics.lowestGrade());
            return ResponseEntity.ok().body(message);
        }
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
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
    public ResponseEntity<String> deleteFirm(@RequestHeader("Authorization") String auth){

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            if(token.getType().equals("Admin") || token.getType().equals("NormalUser")){
                throw new ForbiddenActionException("Firm route, cannot delete user or admin");
            }
            firmService.delete(token.getId());
            return ResponseEntity.ok().body("Account deleted successfully.");    
        } 
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getClass() + ": " + e.getMessage());
        }

    }
}
