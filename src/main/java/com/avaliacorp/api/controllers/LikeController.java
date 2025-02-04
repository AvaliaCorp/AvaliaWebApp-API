package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.services.LikeService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private JwtTools jwtTools;

    @PostMapping("/post")
    public ResponseEntity<String> likePost(@RequestHeader("Authorization") String auth, @RequestParam Integer postId) {

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            likeService.create(token.getId(), postId, null);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post Liked");
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @PostMapping("/comment")
    public ResponseEntity<String> likeComment(@RequestHeader("Authorization") String auth, @RequestParam Integer commentId) {

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            likeService.create(token.getId(), null, commentId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment Liked");
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @GetMapping("/post/count")
    public ResponseEntity<String> countLikesOfPost(@RequestParam Integer postId) {

        try {
            Integer number = likeService.countByPost(postId);
            return ResponseEntity.ok().body("likes: " + number);
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @GetMapping("/comment/count")
    public ResponseEntity<String> countLikesOfComment(@RequestParam Integer commentId) {

        try {
            Integer number = likeService.countByComment(commentId);
            return ResponseEntity.ok().body("likes: " + number);    
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }
    

    @DeleteMapping("/undo/post")
    public ResponseEntity<String> dislikePost(@RequestHeader("Authorization") String auth, @RequestParam Integer postId) {

        try {
            auth = auth.replace("Bearer ","");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            likeService.delete(token.getId(), postId, null);
            return ResponseEntity.ok().body("Post disliked");
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @DeleteMapping("/undo/comment")
    public ResponseEntity<String> dislikeComment(@RequestHeader("Authorization") String auth, @RequestParam Integer commentId) {

        try {
            auth = auth.replace("Bearer ","");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            likeService.delete(token.getId(), null, commentId);
            return ResponseEntity.ok().body("Comment disliked");
        }
        catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

}
