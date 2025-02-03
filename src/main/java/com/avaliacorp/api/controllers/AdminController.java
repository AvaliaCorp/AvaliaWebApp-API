package com.avaliacorp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.UserModel;

import com.avaliacorp.api.services.AdminService;
import com.avaliacorp.api.utils.JwtTools;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService service;
    private JwtTools jwtTools;

    public AdminController(AdminService service, JwtTools jwtTools){
        this.service = service;
        this.jwtTools = jwtTools;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerAdmin(@RequestBody UserModel data) {

        try {
            data.setRole("Admin");
            UserModel user = service.create(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e) {
            if(e instanceof EmailAlreadyInUseException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

    @PutMapping("/edit")
    public ResponseEntity<Object> updateAdmin(@RequestHeader("Authorization") String auth, @RequestBody EditAdminParams params) {

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            if(params.isNull()){
                return ResponseEntity.ok().body("User did not made any changes");
            }
            var user = service.findById(token.getId());
            if(params.password != null) user.setPassword(params.password);
            if(params.email != null) user.setEmail(params.email);
            if(params.name != null) user.setName(params.name);
            service.update(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updated");
        }
        catch (Exception e){
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }
    private record EditAdminParams(String name, String password, String email){
        public boolean isNull(){
            return (name == null && email == null && password == null);
        }
    }

    @PatchMapping("/post/close")
    public ResponseEntity<Object> closePost(@RequestHeader("Authorization") String auth, @RequestBody ClosePostParams params){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            service.closePost(params.postId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post closed");
        }
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }
    private record ClosePostParams(Integer postId){}

    @DeleteMapping("/delete/comment")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String auth, @RequestBody DeleteCommentParams params){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            service.deleteComment(params.commentId);
            return ResponseEntity.ok().body("Comment deleted");
        }
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }
    private record DeleteCommentParams(Integer commentId){}

    @DeleteMapping("/delete/post")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String auth, @RequestBody DeletePostParams params){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            service.deletePost(params.postId);
            return ResponseEntity.ok().body("Post deleted");
        }
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }
    private record DeletePostParams(Integer postId){}

    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String auth, @RequestBody DeleteUserParams params){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            service.delete(params.id);
            return ResponseEntity.ok().body("User deleted");
        }
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }
    private record DeleteUserParams(String id){}

    @DeleteMapping("/delete/firm")
    public ResponseEntity<String> deleteFirm(@RequestHeader("Authorization") String auth, @RequestBody DeleteFirmParams params){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("User is not an Admin");
            }
            service.deleteFirm(params.firmId);
            return ResponseEntity.ok().body("Firm deleted");
        }
        catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }
    }
    private record DeleteFirmParams(String firmId){}

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAdmin(@RequestHeader("Authorization") String auth){

        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("Admin")){
                throw new ForbiddenActionException("The user is not admin");
            }
            service.delete(token.getId());
            return ResponseEntity.ok().body("Admin deleted");
        } catch (Exception e) {
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getClass() + ": " + e.getMessage());
        }

    }

}
