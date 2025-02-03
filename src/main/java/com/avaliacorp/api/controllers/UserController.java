package com.avaliacorp.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.ForbiddenActionException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.models.TokenModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.services.UserService;
import com.avaliacorp.api.utils.JwtTools;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTools jwtTools;

    @PostMapping("/create")
    public ResponseEntity<Object> createNewUser(@RequestBody UserModel data) {

        try {
            data.setRole("NormalUser");
            UserModel user = userService.create(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch(Exception e) {
            if(e instanceof EmailAlreadyInUseException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping(value = "/get")
    public ResponseEntity<Object> getUser(@RequestBody GetUserParams params) {

        try {
            UserModel user;
            if(params.id != null){
                user = userService.findById(params.id);
            }
            else if(params.email != null){
                user = userService.findByEmail(params.email);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Params id or email expected.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/posts")
    public ResponseEntity<Object> getUserPosts(@RequestHeader("Authorization") String auth) {
        jwtTools.init();
        try {
            auth = auth.replace("Bearer ", "");
            var token = jwtTools.verifyAndDecodeToken(auth);
            List<PostModel> posts = userService.findUserPosts(token.getId());
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } 
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String name, @RequestParam Integer limit) {

        try {
            List<UserModel> users = userService.searchUsers(name, limit);
            if(users.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("No users were found.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    public record GetUserParams(String id, String email) {}

    @PutMapping("/edit")
    public ResponseEntity<Object> editUser(@RequestHeader("Authorization") String auth, @RequestBody EditUserParams data) {

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            UserModel user = userService.findById(token.getId());
            if(data.password != null){
                String hashedPassword = BCrypt.withDefaults().hashToString(12, data.password.toCharArray());
                user.setPassword(hashedPassword);
            }
            if(data.email != null) user.setEmail(data.email);
            if(data.name != null) user.setName(data.name.toLowerCase());
            UserModel result = userService.update(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } 
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    public record EditUserParams(String name, String email, String password) {}

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String auth){

        try {
            auth = auth.replace("Bearer ", "");
            TokenModel token = jwtTools.verifyAndDecodeToken(auth);
            if(!token.getType().equals("NormalUser")){
                throw new ForbiddenActionException("User route, cannot delete other types of account");
            }
            userService.delete(token.getId());
            return ResponseEntity.status(HttpStatus.OK).body("User deleted.");
        }
        catch (Exception e) {
            if(e instanceof NotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getClass() + ": " + e.getMessage());
            }
            if(e instanceof ForbiddenActionException){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getClass() + ": " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getClass() + ": " + e.getMessage());
        }

    }
    
}
