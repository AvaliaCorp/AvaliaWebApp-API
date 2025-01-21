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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService service;

    @PostMapping("/create")
    public ResponseEntity<Object> createNewPost(@RequestBody PostModel post) {

        try {
            PostModel result = service.create(post);
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
    
    
}
