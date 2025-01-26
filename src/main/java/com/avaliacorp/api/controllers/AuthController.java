package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.avaliacorp.api.exceptions.LogInFailedException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.EntityModel;
import com.avaliacorp.api.models.LoginModel;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.services.FirmService;
import com.avaliacorp.api.services.LoginService;
import com.avaliacorp.api.services.UserService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    private UserService userService;

    @Autowired
    private FirmService firmService;

    @Autowired
    private JwtTools jwtTools;

    @PostMapping("/login") //Indica que a função mapeia uma rota HTTP do tipo patch chamada /login
    public ResponseEntity<String> login(@RequestBody LoginModel login) { //@RequestBody, indica para o Controller geral do spring que o parametro login deve ser preenchido pelo corpo da requisição

        try {
            if(login.isNull()){ //Se o login for null mande um erro
                throw new IllegalArgumentException("Any of the params cannot be null");
            }
            String token = service.login(login.email(), login.password());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        catch (Exception e) {
            if(e instanceof LogInFailedException){//Trata um erro de tipo LogInFailedException enviando ele como a resposta da requisição
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            if(e instanceof NotFoundException){//Trata um erro de tipo NotFoundException enviando ele como a resposta da requisição
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage() + "\n" + e.getClass());//
        }

    }

    @GetMapping("/user")
    public ResponseEntity<Object> getLoggedUser(@RequestHeader("Authorization") String auth) {

        try {
            String[] tokenLiteral = auth.split(" ");
            DecodedJWT decoded = jwtTools.verifyToken(tokenLiteral[1]);
            TokenModel token = TokenModel.formatDecodedToken(decoded);
            EntityModel<String> requestUser;
            if(token.getType().equals("NormalUser")){
                requestUser = userService.findById(token.getId());
            }
            else {
                requestUser = firmService.findById(token.getId());
            }
            return ResponseEntity.ok().body(requestUser);
        }
        catch (Exception e) {
            
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    

}
