package com.avaliacorp.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.CommentModel;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.services.CommentService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    private CommentService service;
    private JwtTools jwtTools;

    public CommentController(CommentService commentService, JwtTools jwtTools){
        this.service = commentService;
        this.jwtTools = jwtTools;
        jwtTools.init();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewComment(@RequestHeader("Authorization") String auth, @RequestBody CommentModel data) {

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            CommentModel comment = service.create(data, token.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/get/byPost")
    public ResponseEntity<Object> getCommentByPost(@RequestParam Integer postId) {

        try {
            var comments = service.findByPostId(postId);
            return ResponseEntity.status(HttpStatus.OK).body(comments);
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

    @GetMapping("/get/byComment")
    public ResponseEntity<Object> getCommentByComment(@RequestParam Integer commentId) {

        try {
            var comments = service.findByCommentId(commentId);
            return ResponseEntity.status(HttpStatus.OK).body(comments);
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

    @PatchMapping("/update")
    public ResponseEntity<Object> updateComment(@RequestHeader("Authorization") String auth, @RequestBody UpdateCommentParams params) {

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            var comments = service.update(params.id, token.getId(), params.text);
            return ResponseEntity.ok().body(comments);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    record UpdateCommentParams(Integer id, String text){};

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteComment(@RequestHeader("Authorization") String auth, @RequestParam Integer id){

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            service.delete(id, token.getId());
            return ResponseEntity.ok().body("Comment deleted");    
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }
    
}
