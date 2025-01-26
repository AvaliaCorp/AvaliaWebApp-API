package com.avaliacorp.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.services.PostService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService service;

    @Autowired
    private JwtTools jwtTools;

    @PostMapping("/create")
    public ResponseEntity<Object> createNewPost(@RequestHeader("Authorization") String auth, @RequestBody PostModel post) {

        try {
            auth = auth.replace("Bearer ", "");
            PostModel result = service.create(post, auth);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/get")
    public ResponseEntity<Object> getPostById(@RequestParam Integer id) {

        try {
            PostModel post = service.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPosts(@RequestParam String title, Integer limit) {

        try {
            List<PostModel> posts = service.searchByTitle(title, limit);
            if(posts.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("No posts were found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    
    //Rota HTTP do tipo get
    @GetMapping("/search/cnpj")//A função deve pesquisar um posts pelo cnpj da uma epresa
    public ResponseEntity<Object> searchPostsByCNPJ(@RequestParam String CNPJ) {

        try {
            List<PostModel> posts = service.findByFirmCNPJ(CNPJ);
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Rota HTTP do tipo delete
    @DeleteMapping("/delete")//Pega o header Authorization da requisição e espera um parametro de requisição o id do post
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String auth, @RequestParam Integer id){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth); //Verifica e decodifica o token
            service.delete(token.getId(), id);//Ctrl + Click do mouse no delete
            return ResponseEntity.ok().body("Post deleted"); //Resposta de Sucesso: Post deletado
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    
}
