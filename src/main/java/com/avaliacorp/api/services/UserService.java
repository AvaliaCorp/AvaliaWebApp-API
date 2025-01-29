package com.avaliacorp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.PostModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.PostRepository;
import com.avaliacorp.api.repositories.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public UserModel create(UserModel data) throws EmailAlreadyInUseException {
        Optional<UserModel> doesEmailAlreadyInUse = userRepository.findByEmail(data.getEmail());

        if(doesEmailAlreadyInUse.isPresent()){
            throw new EmailAlreadyInUseException();
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, data.getPassword().toCharArray());
        UserModel user = new UserModel(data.getName().toLowerCase(), data.getEmail().toLowerCase(), hashedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public UserModel findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("The email was not found"));
    }

    @Transactional
    public UserModel findById(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("The id was not found"));
    }

    @Transactional
    public List<UserModel> searchUsers(String name, Integer limit){
        return userRepository.findManyByName(name + "%", limit);
    }

    @Transactional
    public List<PostModel> findUserPosts(String id){
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("The user id was not found"));
        return postRepository.findByAuthorId(id);
    }

    @Transactional
    public UserModel update(UserModel data){
        return userRepository.save(data);
    }

    @Transactional
    public void delete(String id){
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("The id was not found"));
        userRepository.deleteById(id);
    }

}
