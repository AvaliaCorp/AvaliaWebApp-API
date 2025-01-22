package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.LogInFailedException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.LoginModel;
import com.avaliacorp.api.services.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe LoginController, ela é reponsável por controlar as relacionadas ao auth (Autenticação, Autorização)
 * A Classe é um RestController, ou seja, é um controlador de rotas http.
 * Essa anotação ´@RestController´ também faz com que o SpringBoot entenda a classe como um componente, ou seja, um objeto a ser instanciado e inicializado automaticamente
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    //Autowired - Indica ao SpringBoot para instaciar automaticamente a variavel
    @Autowired
    private LoginService service;

    @PatchMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel login) {

        try {
            if(login.isNull()){
                throw new IllegalArgumentException("Any of the params cannot be null");
            }
            String token = service.login(login.email(), login.password());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        catch (Exception e) {
            if(e instanceof LogInFailedException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PatchMapping("/logout")
    public ResponseEntity<String> logout() {

        try {
            service.logout();
            return ResponseEntity.ok().body("User unlogged.");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/user")
    public ResponseEntity<Object> getLoggedUser() {

        try {
            var response = service.getUserLogged();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    public record ResponseGetLoggedUser(String id, String type){
        public ResponseGetLoggedUser(String id, String type){
            this.id = id;
            this.type = type;
        }
    }
    

}
