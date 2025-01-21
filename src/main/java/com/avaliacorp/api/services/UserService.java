package com.avaliacorp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avaliacorp.api.exceptions.EmailAlreadyInUseException;
import com.avaliacorp.api.exceptions.UserNotFoundException;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
    public UserModel findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public UserModel findById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public List<UserModel> searchUsers(String name){
        return userRepository.findManyByName(name + "%");
    }

    @Transactional
    public List<UserModel> searchUsers(String name, Integer limit){
        return userRepository.findManyByName(name + "%", limit);
    }

    @Transactional
    public UserModel update(UserModel data){
        return userRepository.save(data);
    }

    @Transactional
    public void delete(String id){
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        userRepository.deleteById(id);
    }

}
