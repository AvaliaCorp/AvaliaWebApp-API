package com.avaliacorp.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.AnswerModel;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.services.AnswerService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/firm-answer")
public class AnswerController {

    private AnswerService service;
    private JwtTools jwtTools;

    public AnswerController(AnswerService service, JwtTools jwtTools){
        this.service = service;
        this.jwtTools = jwtTools;
        jwtTools.init();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAnswer(@RequestHeader("Authorization") String auth, @RequestBody AnswerModel answerModel) {

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            if(token.getType().equals("NormalUser")){
                throw new ForbiddenActionException("Normal User shall not post a firm-answer");
            }
            AnswerModel answer = service.create(answerModel, token.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(answer);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.badRequest().body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.badRequest().body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @GetMapping("/get/byPost")
    public ResponseEntity<Object> getByPost(@RequestParam Integer postId) {

        try {
            List<AnswerModel> answers = service.findByPostId(postId);
            return ResponseEntity.ok().body(answers);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.badRequest().body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String auth, @RequestBody DeleteAnswerParams param){

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            service.delete(token.getId(), param.id);
            return ResponseEntity.ok().body("deleted");
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.badRequest().body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.badRequest().body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }
    public record DeleteAnswerParams(Integer id){}

}
